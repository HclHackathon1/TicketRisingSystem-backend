---
name: generate-feature
description: Use this skill when generating new code, features, or architectural components to enforce strict modular boundaries.
---
# Objective
Enforce strict modular architecture to minimize conflicts and ensure clean code generation.

## Global System Rules
- Never push directly to main.
- Always use feature branches.
- Always follow the modular architecture defined below.
- Avoid modifying shared files unnecessarily.
- Prefer additive changes over destructive ones.

## Architecture Rules
- Each feature owns its folder.
- No cross-module edits.
- Feature → common is allowed.
- Feature → feature is NOT allowed.
- Shared configs should be edited cautiously.

## Code Generation Guidelines
- Always create files inside the specific module folder (controller, service, repo, dto).
- Avoid modifying existing shared files; prefer new files over editing old ones.
- Separate folders per feature to prevent merge conflicts.
- Do not overload shared services.