# Contributing
## Review process
1. A pull request addresses a single issue. 

2. A pull request must be approved by two reviewers.

3. A pull request is squashed before the merge.

## Naming Conventions: Issues, branches, pull requests, squashed merge commits
1. Issues describe project work such as tasks, bugs, and feature requests. 
Example: ```Implement task XYZ```

2. The branch name follows the issue name. 
The automatically generated issue ID is succeeded by the issue name in lowercase letters, with hyphens separating multiple words.
Example: ```#1-implement-task-xyz```

3. The pull request name is the issue name plus the issue id.
Example: ```Implement task XYZ (Issue #1)```

4. The squashed merge commit name is the issue name plus the issue id plus the pull request id. 
Example: ```Implement task XYZ (Closes #1) (PR #2)```

5. If a pull request addresses several issues, the pull request name is a summary of the issues plus the issue ids.
The squashed merge commit name is a summary of the issues plus the issue ids plus the pull request id.
Example pull request: ```Initial Setup (Issue #1, Issue #2, Issue #3)```
Example squashed merge commit: ```Initial Setup (Closes #1, Closes #2, Closes #3) (PR #2)```
