# FreeOrion browser port: status and implementation plan

## Current status

The repository's main client is a native C++20 application. The current build expects desktop-oriented OpenGL, GiGi, SDL2, OpenAL, Boost, Python/libpython, and other libraries. GitHub Pages cannot run the native executable as-is.

A browser gameplay slice is now available at [the Browser Lab](../play/). It is a **separate, simplified prototype**, not a port of the FreeOrion engine. It currently demonstrates:

- An interactive star map with selectable systems and procedural starfield rendering.
- Turn advancement and empire resource income.
- Moving a small fleet between systems.
- Colonizing unclaimed systems when the fleet is present.
- Building scout ships, funding research, and advancing a small technology tree.
- Browser-local save/load using localStorage.
- A custom Pages entry point and a 404 recovery page.

The Browser Lab intentionally uses original, simplified sample system data and rules. It does not load or execute the native C++ game, FreeOrion Content Scripts (FOCS), original AI, full combat, diplomacy, multiplayer/server networking, native save files, or the complete asset and localization catalogs.

## Work still required for the full game

A real browser port is a multi-stage engine and dependency project, not just a Pages configuration change.

1. **Build a compatibility inventory.** Identify native-only APIs and dependency versions across the client, server, GiGi, OpenGL, SDL2, OpenAL, Python integration, file-system/resource loading, threading, and networking.
2. **Create an Emscripten toolchain target.** Add a separate experimental CMake toolchain/configuration and compile a minimal native-independent C++ smoke test to WebAssembly before attempting the full application.
3. **Port the rendering and input layer.** Map the rendering path to WebGL/WebGL2 and browser input, viewport scaling, focus, and context-loss handling. The existing OpenGL/GiGi code cannot be assumed to work unchanged.
4. **Resolve scripting.** Port or replace Python/libpython integration and validate FOCS parsing/runtime behavior in a browser-compatible environment.
5. **Package content deliberately.** Build a minimized, licensed resource bundle with predictable virtual filesystem paths; avoid shipping native binaries or unnecessary development files.
6. **Port persistence and platform services.** Implement browser-safe save/load with IndexedDB or an equivalent persistent filesystem, and adapt audio, timing, threading, and network transport to browser restrictions.
7. **Integrate real game logic.** Connect the browser client to the actual FreeOrion simulation, content rules, AI, diplomacy, combat, and turn-resolution systems. The simplified Browser Lab is not a substitute for these systems.
8. **Test and publish.** Add deterministic simulation tests, WebAssembly smoke tests, browser automation, memory/performance checks, and end-to-end deployment verification. Publish only generated static output from a dedicated directory.

## Deployment

The GitHub Pages workflow publishes the static contents of `docs/`. The browser slice is therefore available at the relative route `play/` once Pages is enabled and the workflow succeeds. Check the [Pages workflow](../../actions/workflows/pages.yml) and [Pages settings](../../settings/pages) for the actual deployment state.

A successful static-site deployment proves only that the website and prototype were published; it does not prove that the complete native game runs in a browser.

## License and provenance

The FreeOrion source code is GPL-2.0 and game assets/content include CC BY-SA 3.0 terms as described in the root README and `default/COPYING`. Before packaging original resources in a web build, preserve required notices and confirm licenses for all bundled third-party dependencies. The Browser Lab's sample star map and rules are a new, simplified demonstration and do not bundle the native asset catalog.
