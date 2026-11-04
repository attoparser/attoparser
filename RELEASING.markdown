# Releasing Attoparser

## 1. Configuration

Configure access to the Central Portal in `settings.xml`. `central` is used for
publishing releases, and `central-portal-snapshots` for publishing snapshots:

```xml
  <servers>
    <server>
      <id>central</id>
      <username>[USER TOKEN IN CENTRAL PORTAL]</username>
      <password>[ENCODED PASSWORD TOKEN IN CENTRAL PORTAL]</password>
    </server>
    <server>
      <id>central-portal-snapshots</id>
      <username>[USER TOKEN IN CENTRAL PORTAL]</username>
      <password>[ENCODED PASSWORD TOKEN IN CENTRAL PORTAL]</password>
      <repositoryOrigins>
        <repositoryOrigin>https://central.sonatype.com</repositoryOrigin>
      </repositoryOrigins>
    </server>
  </servers>
```

`repositoryOrigins` (Maven 3.10+) binds the snapshot credentials to the Central Portal
host, as the snapshots repository is declared in `pom.xml`.

## 2. Check dependency snapshots

If this is an GA or RC version of Attoparser being published, check that no
dependencies are in SNAPSHOT version.

If any SNAPSHOTs exist, set to a fixed version and commit.

## 3. Set the new version

```shell
mvn versions:set -DprocessAllModules=true -DnewVersion=X.Y.Z
mvn versions:commit
git add .
git commit -m "Prepare release attoparser-X.Y.Z"
git push
```

## 4. Create and deploy the release into the staging repositories

```shell
mvn -P releases clean compile deploy
```

## 5. Create tag and set to new development version

```shell
mvn scm:tag -Dtag=attoparser-X.Y.Z
mvn versions:set -DprocessAllModules=true -DnewVersion=X.Y.[Z+1]-SNAPSHOT
mvn versions:commit
git add .
git commit -m "Prepare for next development iteration"
git push
```

## 6. Create tag and set to new development version

If any SNAPSHOT dependencies had to have their versions fixed before building, go
back to SNAPSHOT versions wherever needed and commit.

## 7. Manage staging repository in Central

Follow instructions at https://central.sonatype.org/publish/publish-guide/

## 8. Releasing distribution artifacts

Once jar artifacts have been published in Maven Central, the .zip distribution release
generated in dist/target should be uploaded to the GitHub repository as a new release
along with a gpg signature (.asc) generated with:

```shell
gpg -ab --default-key key_ID_for_releases@attoparser.org attoparser-X.Y.Z-dist.zip
```



