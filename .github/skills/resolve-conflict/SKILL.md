---
name: resolve-conflict
description: Use this skill when the user encounters git merge conflicts and needs strategic help resolving them.
---
# Objective
Safely resolve git merge conflicts and prepare the branch for a successful merge.

## Resolution Strategy
Analyze the conflicting files and apply the appropriate strategy:

A. **Structural Conflicts** (e.g., imports, folder changes) → Merge both, ensuring paths are correct.
B. **Additive Conflicts** (e.g., two new methods in the same class) → Keep both additions.
C. **Config Conflicts** (e.g., application.yml, package.json) → Combine carefully, do not arbitrarily overwrite.
D. **Logical Conflicts** (e.g., both changed the same core business logic) → Escalate to the user. Do not guess the intent.

## Workflow Steps
1. Verify current state: `git status`
2. Analyze conflict markers (`<<<<<<<`, `=======`, `>>>>>>>`).
3. Apply the matching strategy (A, B, C, or D).
4. Stage the resolved files: `git add .`
5. Finalize the merge: `git commit -m "merge(main): resolved conflicts"`
6. Push the updated branch: `git push origin <branch-name>`

## Constraints
- Do not overwrite changes blindly.
- Ensure the resolution does not break existing APIs or compilation.