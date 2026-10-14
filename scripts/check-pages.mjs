import { readFileSync, existsSync, writeFileSync, unlinkSync } from "node:fs";
import { spawnSync } from "node:child_process";

const requiredFiles = ["docs/index.html", "docs/404.html", "docs/.nojekyll", "docs/play/index.html"];
const missing = requiredFiles.filter((file) => !existsSync(file));
if (missing.length) {
  console.error("GitHub Pages files are missing:", missing.join(", "));
  process.exit(1);
}

const html = readFileSync("docs/index.html", "utf8");
const notFound = readFileSync("docs/404.html", "utf8");
const browserGame = readFileSync("docs/play/index.html", "utf8");
if (!/<html\s+lang=["']en["']/i.test(html)) {
  throw new Error("docs/index.html must declare its document language.");
}
if (!/<main\b/i.test(html) || !/<title>[^<]+<\/title>/i.test(html)) {
  throw new Error("docs/index.html must include a main landmark and a title.");
}
if (!/404/.test(notFound) || !/href=["']\.\/["']/.test(notFound)) {
  throw new Error("docs/404.html must identify the 404 and link back to the site root.");
}
if (!/href=["']\.\/play\//.test(html)) {
  throw new Error("The project portal must link to the browser lab.");
}
for (const control of ["colonize", "buildShip", "moveFleet", "researchBtn", "turnBtn", "saveBtn", "loadBtn"]) {
  if (!browserGame.includes('id="' + control + '"')) {
    throw new Error("Browser lab is missing required control: " + control);
  }
}
if (!/localStorage\.setItem/.test(browserGame) || !/function endTurn\(/.test(browserGame)) {
  throw new Error("Browser lab must include turn progression and local save support.");
}

const ids = new Set([...html.matchAll(/\bid=["']([^"']+)["']/g)].map((match) => match[1]));
const missingAnchors = [...html.matchAll(/href=["']#([^"']+)["']/g)]
  .map((match) => match[1])
  .filter((anchor) => !ids.has(anchor));
if (missingAnchors.length) {
  throw new Error("Broken in-page links in docs/index.html: " + [...new Set(missingAnchors)].join(", "));
}

// Parse the embedded browser-game JavaScript with the same Node runtime used by CI.
const scriptMatch = browserGame.match(/<script>([\s\S]*?)<\/script>/i);
if (!scriptMatch) throw new Error("Browser lab has no inline JavaScript entry point.");
const checkFile = ".freeorion-browser-check.cjs";
try {
  writeFileSync(checkFile, scriptMatch[1]);
  const syntax = spawnSync(process.execPath, ["--check", checkFile], { encoding: "utf8" });
  if (syntax.status !== 0) {
    throw new Error("Browser game JavaScript syntax check failed:\n" + (syntax.stderr || syntax.stdout));
  }
} finally {
  if (existsSync(checkFile)) unlinkSync(checkFile);
}

console.log("Pages validation passed: required files, portal navigation, browser-game controls, save/turn logic, HTML landmarks, in-page links, and JavaScript syntax.");
