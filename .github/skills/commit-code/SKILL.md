---
name: commit-code
description: Use this skill to automatically analyze changes and execute the git commit in the terminal.
---
# Objective
Analyze workspace changes and execute a git commit autonomously.

## Execution Steps (USE TERMINAL TOOL)
You must use your terminal execution tool to run these commands sequentially:
1. Run `git status` and `git diff` to analyze the changes.
2. Group the changes logically.
3. Generate a Conventional Commit message (e.g., `feat(auth): add login route`).
4. Execute `git add .`
5. Execute `git commit -m "<your generated message>"`

Do not just suggest the commands in chat. You must execute them in the terminal.