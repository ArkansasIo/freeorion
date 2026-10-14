import { readFileSync, existsSync } from "node:fs";

const requiredFiles = ["docs/index.html", "docs/404.html", "docs/.nojekyll"];
const missing = requiredFiles.filter((file) => !existsSync(file));
if (missing.length) {
  console.error("GitHub Pages files are missing:", missing.join(", "));
  process.exit(1);
}

const html = readFileSync("docs/index.html", "utf8");
const notFound = readFileSync("docs/404.html", "utf8");
if (!/<html\s+lang=["']en["']/i.test(html)) {
  throw new Error("docs/index.html must declare its document language.");
}
if (!/<main\b/i.test(html) || !/<title>[^<]+<\/title>/i.test(html)) {
  throw new Error("docs/index.html must include a main landmark and a title.");
}
if (!/404/.test(notFound) || !/href=["']\.\/["']/.test(notFound)) {
  throw new Error("docs/404.html must identify the 404 and link back to the site root.");
}

const ids = new Set([...html.matchAll(/\bid=["']([^"']+)["']/g)].map((match) => match[1]));
const missingAnchors = [...html.matchAll(/href=["']#([^"']+)["']/g)]
  .map((match) => match[1])
  .filter((anchor) => !ids.has(anchor));
if (missingAnchors.length) {
  throw new Error("Broken in-page links in docs/index.html: " + [...new Set(missingAnchors)].join(", "));
}

console.log("Pages validation passed: entry point, custom 404, .nojekyll, document landmarks, and in-page links.");
