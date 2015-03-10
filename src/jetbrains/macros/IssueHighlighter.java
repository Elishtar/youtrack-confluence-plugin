package jetbrains.macros;

import com.atlassian.bandana.BandanaManager;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.renderer.v2.macro.MacroException;
import jetbrains.macros.base.YouTrackAuthAwareMacroBase;
import jetbrains.macros.util.SettingsManager;
import jetbrains.macros.util.Strings;
import youtrack.CommandBasedList;
import youtrack.Issue;
import youtrack.Project;
import youtrack.YouTrack;
import youtrack.util.IssueId;

import java.util.Map;

public class IssueHighlighter extends YouTrackAuthAwareMacroBase {

    public IssueHighlighter(BandanaManager bandanaManager) {
        super(bandanaManager);
    }

    public boolean isInline() {
        return true;
    }

    public boolean hasBody() {
        return false;
    }

    public RenderMode getBodyRenderMode() {
        return RenderMode.NO_RENDER;
    }

    public String execute(Map params, String body, RenderContext renderContext)
            throws MacroException {
        try {
            final Map<String, Object> context = MacroUtils.defaultVelocityContext();
            final String issueId = (String) params.get(Strings.ID);
            String style = (String) params.get(Strings.STYLE);
            if (!Strings.DETAILED.equals(style)) {
                style = Strings.SHORT;
            }
            if (issueId != null && !issueId.isEmpty()) {
                IssueId id = new IssueId(issueId);
                CommandBasedList<YouTrack, Project> projects = youTrack.projects;
                final Project project = tryGetItem(projects, id.projectId);
                if (project != null) {
                    Issue issue = tryGetItem(project.issues, issueId);
                    if (issue != null) {
                        issue = issue.createSnapshot();
                        context.put(Strings.ISSUE, issueId);
                        context.put(Strings.SUMMARY, issue.getSummary());
                        context.put(Strings.BASE, SettingsManager.getInstance(bm).getStoredHost().replace(Strings.REST_PREFIX, SettingsManager.EMPTY_STRING));
                        context.put(Strings.STYLE, (issue.isResolved()) ? "line-through" : "normal");
                        context.put("title", "Reporter: " + issue.getReporter() + ", Priority: " + issue.getPriority() + ", State: " +
                                issue.getState() + ", Assignee: " +
                                (issue.getAssignee() != null ? issue.getAssignee().getFullName() : Strings.UNASSIGNED) +
                                ", Votes: " + issue.getVotes() + ", Type: " + issue.getType());
                    } else context.put(Strings.ERROR, "Issue not fount: " + issueId);
                } else {
                    context.put(Strings.ERROR, "Project not found: " + id.projectId);
                }
            } else {
                context.put(Strings.ERROR, "Missing id parameter");
            }
            return VelocityUtils.getRenderedTemplate((Strings.SHORT.equals(style) ? Strings.BODY_LINK : Strings.BODY_DETAILED), context);
        } catch (Exception ex) {
            throw new MacroException(ex);
        }
    }
}