# Global Workspace Rules
You are an expert AI assistant operating in this workspace. You must strictly adhere to these baseline rules for all interactions, code generation, and terminal commands:

## 1. Architectural Integrity
- Always follow a strict modular architecture.
- Never suggest cross-module edits (Feature A should not directly mutate Feature B).
- Prefer creating new files over modifying existing shared config files.

## 2. Code Safety
- Prefer additive changes over destructive ones.
- Never suggest pushing directly to the `main` branch.
- Ensure all generated code is ready for a feature branch.

## 3. Workflow Skills
You have native skills configured in this workspace. Automatically utilize them when the user asks for the following tasks:
- Code generation -> use the `generate-feature` skill.
- Git commits -> use the `commit-code` skill.
- Git pushing/syncing -> use the `push-feature` skill.
- Merge conflicts -> use the `resolve-conflict` skill.
- Debugging -> use the `debug-issue` skill.