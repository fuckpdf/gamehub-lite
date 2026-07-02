.class public final Lcom/xj/common/utils/SensitiveLogRedactor;
.super Ljava/lang/Object;
.source "SourceFile"


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static final a(Ljava/lang/String;)Ljava/lang/String;
    .locals 2

    if-nez p0, :cond_0

    return-object p0

    :cond_0
    const-string v0, "(?i)((?:\"?)(?:steamToken|accessToken|refreshToken|access_token|refresh_token|pay_token|yunxin_token|sessionid|steamLoginSecure|steamLogin|steamMachineAuth|token)(?:\"?)\\s*[:=]\\s*[\"']?)[^\"',)\\]\\}\\s]+"

    const-string v1, "$1[REDACTED]"

    invoke-virtual {p0, v0, v1}, Ljava/lang/String;->replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    const-string v0, "(?i)(--(?:token|password)\\s+)[^\\s]+"

    const-string v1, "$1[REDACTED]"

    invoke-virtual {p0, v0, v1}, Ljava/lang/String;->replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    const-string v0, "(?i)(https://s\\.team/q/)[A-Za-z0-9_-]+"

    const-string v1, "$1[REDACTED]"

    invoke-virtual {p0, v0, v1}, Ljava/lang/String;->replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    const-string v0, "\\b[A-Za-z0-9_-]{20,}\\.[A-Za-z0-9_-]{20,}\\.[A-Za-z0-9_-]{20,}\\b"

    const-string v1, "[REDACTED_JWT]"

    invoke-virtual {p0, v0, v1}, Ljava/lang/String;->replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method
