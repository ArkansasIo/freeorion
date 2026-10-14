# FreeOrion browser deployment notes

This fork contains the native FreeOrion C++ game source. GitHub Pages can host static web assets, but it cannot execute the native desktop client directly. Do not treat the repository root as a Pages site or publish the full C++ source tree as a web app.

## Current status

- Native project: C++ with CMake and platform-specific build workflows.
- Native rendering/runtime dependencies include OpenGL, SDL2, OpenAL, Python, Boost, and other libraries (see `BUILD.md`).
- Repository Pages setting: currently not enabled (verify in Settings → Pages before expecting a published URL).
- Actions runs were not returned by the repository API at the time these notes were written; this does not prove that native builds fail.

## Recommended route to a real browser build

1. Keep the existing native build and packaging workflows unchanged.
2. Create a separate experimental browser-port target rather than trying to run the desktop executable in Pages.
3. Audit browser compatibility of the rendering/input/audio stack and the Python integration. The current native OpenGL and desktop API assumptions are likely to require a port or replacement.
4. Build the compatible C++ subset with Emscripten to WebAssembly/WebGL, with explicit filesystem/resource packaging for `default/` data files.
5. Start with a small smoke-test target and minimal asset bundle before attempting to port the complete client. Validate memory use, save/load behavior, audio, input, networking, and browser security constraints.
6. Publish only the produced static output (`.html`, `.js`, `.wasm`, required data files) from a dedicated directory or a separate Pages branch using a GitHub Actions Pages workflow. Never publish CMake build files or secrets.
7. Verify the deployed URL and browser console after deployment; a green upload job by itself is not proof the game runs.

## Current Pages behavior

There is no ready-to-publish browser build directory established by this note. Enabling Pages now would not convert the native game into a browser game. Once a browser build exists, configure GitHub Pages to deploy its generated static output, not the root source tree.

## Source and licensing

Review the root `README.md`, `BUILD.md`, `default/COPYING`, and the licenses of bundled third-party libraries and assets before redistributing any web build. A browser port may introduce additional distribution and attribution requirements.
