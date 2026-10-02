---
name: github-actions-ci-cd
description: Authoritative guidelines, best practices, and troubleshooting for GitHub Actions and CI/CD pipelines in PluginKit, focusing on Gradle builds, convention plugins, and Version Catalog deployment to GitHub Packages Maven.
---

# GitHub Actions CI/CD Specialist - PluginKit

This skill provides authoritative guidelines for designing, maintaining, and troubleshooting GitHub Actions CI/CD workflows for the **PluginKit** repository.

---

## 1. Project Deployment Context

PluginKit uses GitHub Actions for two core responsibilities:
1. **Continuous Integration (CI)**: Static analysis, code formatting checks, and compilation of all plugins and sample consumer modules (`showcase`, `mylibrary`, `myjvmlibrary`).
2. **Continuous Deployment (CD)**: Publishing compiled Maven artifacts to **GitHub Packages (`maven.pkg.github.com`)**:
   - `:build-logic:publish` -> Convention Plugins (`pluginkit.*`)
   - `:gradle-catalog:publish` -> Gradle Version Catalog (`es.joshluq.kit:catalog`)

---

## 2. GitHub Packages Publishing Requirements

### Job Permissions (`permissions`)
Workflows publishing artifacts to GitHub Packages must declare explicit write permissions:

```yaml
permissions:
  contents: write
  packages: write
```

> [!IMPORTANT]
> Omitting `packages: write` will cause Gradle to fail with `HTTP 401 Unauthorized` or `HTTP 403 Forbidden` during artifact upload.

### Environment Variables & Credentials
Gradle build scripts (`build-logic/build.gradle.kts` and `gradle-catalog/build.gradle.kts`) expect the following environment variables:
- `GITHUB_REPOSITORY`: Repository name in `owner/repo` format (provided automatically by GitHub Actions).
- `GITHUB_ACTOR`: The GitHub user triggering the action.
- `GITHUB_TOKEN`: The workflow-generated secret (`${{ secrets.GITHUB_TOKEN }}`).

```yaml
- name: Publish Artifacts
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
    GITHUB_ACTOR: ${{ github.actor }}
    GITHUB_REPOSITORY: ${{ github.repository }}
  run: |
    chmod +x gradlew
    ./gradlew :build-logic:publish :gradle-catalog:publish -PpluginVersion="<VERSION>" --no-daemon --stacktrace
```

---

## 3. Recommended Gradle Step Standards

Always use official, performance-optimized GitHub Actions with caching:

```yaml
- name: Checkout Code
  uses: actions/checkout@v4
  with:
    fetch-depth: 0

- name: Setup JDK 17
  uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'

- name: Setup Gradle
  uses: gradle/actions/setup-gradle@v3
```

---

## 4. Repository Workflow Architecture

The repository completely decouples continuous **Snapshot** publishing from official **Releases**, removing the need for manual version-bump commits:

### A. Snapshot Publishing (`publish-snapshots.yml`)
* **Trigger**: Push / Merge to the `develop` branch.
* **Behavior**:
  1. Reads `pluginKitVersion` from `gradle.properties` (e.g., `2.0.0`).
  2. Appends `-SNAPSHOT` dynamically (e.g., `2.0.0-SNAPSHOT`).
  3. Publishes to GitHub Packages with `-PpluginVersion="${VERSION}-SNAPSHOT"`.

### B. Official Release Publishing (`publish-release.yml`)
* **Trigger**: Push / Merge to the `main` branch.
* **Behavior**:
  1. Reads clean `pluginKitVersion` from `gradle.properties` (e.g., `2.0.0`).
  2. Publishes to GitHub Packages with `-PpluginVersion="2.0.0"`.
  3. Automatically creates the Git Tag (e.g., `v2.0.0`) and the **GitHub Release** with auto-generated release notes and changelog.

### C. Continuous Verification in PRs (`pr-checks.yml`)
* **Trigger**: Pull Request targeting `main` or `develop`.
* **Behavior**: Executes:
  ```bash
  ./gradlew :build-logic:check :showcase:assembleDebug :mylibrary:assemble :myjvmlibrary:build --no-daemon
  ```

---

## 5. Common Troubleshooting & Error Resolution

1. **`HTTP 401 / 403 Forbidden` on publication**:
   - Ensure the workflow job includes `permissions: packages: write`.
   - Verify that GitHub Packages permissions are enabled in repository settings (`Settings -> Actions -> General -> Workflow permissions -> Read and write permissions`).
2. **`Permission Denied: ./gradlew`**:
   - The Gradle wrapper script lacks executable permissions on Linux. Always run `chmod +x gradlew` before invoking it.
3. **Case Sensitivity in Package URLs**:
   - GitHub Packages URLs are case-sensitive on the repository owner/name path (`https://maven.pkg.github.com/owner/repo`). Ensure `GITHUB_REPOSITORY` matches the exact case of the GitHub repository URL.
4. **`HTTP 409 Conflict` on release publishing**:
   - GitHub Packages enforces immutability for official release artifacts (non-`-SNAPSHOT`). If a version tag or publication was already partially uploaded or previously published, GitHub Packages will reject re-uploading identical POM or metadata files with `409 Conflict`.
   - **Resolution**: Either delete the existing conflicting version under repository *Packages -> Package settings -> Manage versions*, or increment the version (`pluginKitVersion=X.Y.Z`) before triggering the release workflow.

