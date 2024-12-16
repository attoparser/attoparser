# Security Policy

## Reporting Vulnerabilities

Thank you for your collaboration keeping Attoparser safe and secure. If you believe you have found a security
issue in Attoparser, please notify us so that we can work with you in its prompt resolution.

### Disclosure Policy

* Let us know as soon as possible by sending an email to [security@attoparser.org][security-email].
* Provide us a reasonable amount of time to resolve the issue before any disclosure to the public or a
  third-party. Especially, **do not** create a GitHub issue ticket yourself talking about the
  vulnerability. We may publicly disclose the issue _before_ resolving it, but only if appropriate.

### Credit

We will credit the reporter of a confirmed vulnerability in the GitHub ticket created for publishing it (typically
once it is fixed).

### Exclusions

We reserve the right to consider out of the scope of Attoparser's security:

* Developer bad practices and inadequate uses of Attoparser that effectively _create_ the vulnerability in
  the applications being developed with Attoparser.
* Attacks requiring physical access to the machine Attoparser is running on.
* Issues in Attoparser's software dependencies which should be reported to these dependencies' maintainers.


# Supported Versions

Versions of Attoparser look like X.Y.Z, meaning X = Major, Y = Minor and Z = patch. We recommend 
keeping Attoparser dependencies in applications upgraded to the latest patch version.

Also please note that, in general, only the latest minor version will be considered supported
and will receive timely updates and security patches.


# Artifact Signatures

Published artifacts for Attoparser, such as JARs released on Maven Central, are signed with GPG keys that
can be verified at the [Attoparser Organization Repository][gpg-keys]

[security-email]: mailto:security@attoparser.org
[gpg-keys]: https://github.com/attoparser/attoparser-org/blob/main/GPG-KEYS-attoparser.txt
