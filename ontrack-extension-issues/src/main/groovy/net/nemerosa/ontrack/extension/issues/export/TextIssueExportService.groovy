package net.nemerosa.ontrack.extension.issues.export

import net.nemerosa.ontrack.extension.issues.IssueServiceExtension
import net.nemerosa.ontrack.extension.issues.model.Issue
import net.nemerosa.ontrack.extension.issues.model.IssueServiceConfiguration
import org.springframework.stereotype.Component

@Component
class TextIssueExportService extends AbstractTextIssueExportService {

    TextIssueExportService() {
        super(ExportFormat.TEXT)
    }

    @Override
    void exportAsText(IssueServiceExtension issueServiceExtension, IssueServiceConfiguration issueServiceConfiguration, Map<String, List<Issue>> groupedIssues, StringBuilder s) {
        groupedIssues.each { groupName, issues ->
            // Group header
            if (groupName) {
                s << "${groupName}\n\n"
            }
            // List of issues
            issues.each { issue ->
                s << "* ${issue.displayKey} ${issue.summary}\n"
            }
            // Group separator
            s << '\n'

        }
    }
}
