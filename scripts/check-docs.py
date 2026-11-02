#!/usr/bin/env python3
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SKIP_DIRS = {"build", ".gradle", ".kotlin", ".idea", ".git", "_to_delete", "node_modules"}
LINK = re.compile(r"(?<!!)\[[^\]]*\]\(([^)\s]+)(?:\s+\"[^\"]*\")?\)")
SKILL_NAME = re.compile(r"^[a-z0-9]+(-[a-z0-9]+)*$")
ADR_FILE = re.compile(r"^\d{4}-[a-z0-9-]+\.md$")

errors = []


def markdown_files():
    for path in ROOT.rglob("*.md"):
        if not SKIP_DIRS.intersection(path.relative_to(ROOT).parts):
            yield path


def strip_code(text):
    text = re.sub(r"```.*?```", "", text, flags=re.S)
    return re.sub(r"`[^`\n]*`", "", text)


def check_links(path):
    for target in LINK.findall(strip_code(path.read_text(encoding="utf-8"))):
        if re.match(r"^[a-z]+:", target) or target.startswith("#"):
            continue
        relative = target.split("#", 1)[0]
        if relative and not (path.parent / relative).exists():
            errors.append(f"{path.relative_to(ROOT)}: broken link -> {target}")


def frontmatter(text):
    match = re.match(r"^---\n(.*?)\n---\n", text, flags=re.S)
    if not match:
        return None, text
    fields, key = {}, None
    for line in match.group(1).splitlines():
        top = re.match(r"^([a-z-]+):\s*(.*)$", line)
        if top:
            key = top.group(1)
            fields[key] = top.group(2).strip()
    return fields, text[match.end():]


def check_skills():
    skills_dir = ROOT / ".agents" / "skills"
    if not skills_dir.is_dir():
        return
    for skill in sorted(p for p in skills_dir.iterdir() if p.is_dir()):
        skill_md = skill / "SKILL.md"
        where = skill_md.relative_to(ROOT)
        if not skill_md.is_file():
            errors.append(f"{skill.relative_to(ROOT)}: missing SKILL.md")
            continue
        fields, body = frontmatter(skill_md.read_text(encoding="utf-8"))
        if fields is None:
            errors.append(f"{where}: missing YAML frontmatter")
            continue
        name = fields.get("name", "")
        description = fields.get("description", "")
        if name != skill.name:
            errors.append(f"{where}: name '{name}' must match directory '{skill.name}'")
        if not SKILL_NAME.match(name) or len(name) > 64:
            errors.append(f"{where}: invalid name '{name}'")
        if not 1 <= len(description) <= 1024:
            errors.append(f"{where}: description must have 1-1024 characters")
        if len(body.splitlines()) > 500:
            errors.append(f"{where}: body longer than 500 lines")


def check_adr_index():
    decisions = ROOT / "docs" / "decisions"
    if not decisions.is_dir():
        return
    index = (decisions / "README.md").read_text(encoding="utf-8")
    for adr in sorted(decisions.iterdir()):
        if ADR_FILE.match(adr.name) and f"({adr.name})" not in index:
            errors.append(f"docs/decisions/README.md: {adr.name} missing from the index")


def check_agents_size():
    for path in markdown_files():
        if path.name == "AGENTS.md" and len(path.read_text(encoding="utf-8").splitlines()) > 150:
            errors.append(f"{path.relative_to(ROOT)}: longer than 150 lines")


for md in markdown_files():
    check_links(md)
check_skills()
check_adr_index()
check_agents_size()

if errors:
    print("Documentation check failed:", file=sys.stderr)
    for error in errors:
        print(f"  {error}", file=sys.stderr)
    sys.exit(1)
print("Documentation check passed.")
