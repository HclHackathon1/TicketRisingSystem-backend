---
name: push-feature
description: Use this skill to automatically sync with main and execute a git push to the remote repository.
---
# Objective
Safely sync the feature branch with main and push it to the remote repository autonomously.

## Execution Steps (USE TERMINAL TOOL)
You must use your terminal execution tool to run these commands sequentially. If any command fails, stop and report the error to the user.
1. Execute `git checkout main`
2. Execute `git pull origin main`
3. Execute `git checkout -` (to return to the feature branch)
4. Execute `git merge main`
5. If the merge is successful, execute `git push -u origin HEAD`

Do not just suggest the commands. Execute them via the terminal tool.