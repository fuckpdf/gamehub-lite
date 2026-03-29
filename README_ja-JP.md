# GameHub Lite

## GameHub Liteとは何ですか？

GameHub Liteは、教育目的のためにコミュニティによって維持と管理されているGameHubの改良版です。

---

## GameHub Lite Patcher

GameHub 5.10をGameHub Liteに変換するパッチ適用システムです。プライバシーを重視した軽量版でテレメトリによる情報収集機能の削除、オフラインサポートを追加しています。

## GameHub Liteは何が違うんですか？

GameHub Liteは、GameHubを改良したバージョンになります:

- **テレメトリとトラッキングを削除** - Umeng analytics、Firebase、JPush、クラッシュレポートを削除
- **ブロートを削除** - クラウドストリーミング、Xboxクラウドゲーミング、ソーシャル機能などを削除
- **アカウント要求を削除** - ログイン不要でアプリが使用できます
- **アプリのサイズを削減** - 118MBから52MBに削減 (-56%)
- **不要な権限を削除** - 位置情報、連絡先、電話の状態などの権限を削除
- **オフラインサポートを追加** - ネットワーク接続なしでも動作可能になっています
- **パッケージ名を変更** - サイドバイサイドインストール用に`gamehub.lite`に変更
- **カスタムニュースページを追加** - アップデートでコミュニティAPIを使用可能にします

## クイックスタート

### 前提条件

必要なツールをインストールしてください:

`adb`は、Android Studioまたは`platform-tools`パッケージから入手できます。

```bash
# macOS
brew install apktool openjdk

# Ubuntu/Debian
sudo apt install apktool openjdk-17-jdk

# Arch
sudo pacman -S apktool jdk17-openjdk

# Fedora (dnf):
sudo dnf install apktool java-17-openjdk

# Windows
stop using windows
# JK.
# apktoolを https://apktool.org/ ダウンロード
# OracleまたはAdoptOpenJDK 17以降をインストールしてください
# 未テストですが、WSL2でも動作するはずです

```

### パッチを適用

1. GameHub 5.1.0のAPKをダウンロードし、`apk/GameHub-5.1.0.apk`に配置

2. Patcherを実行:

```bash
./patch.sh
```

3. 出力されたAPKをインストール:

```bash
adb install output/GameHub-Lite.apk
```

#### カスタムキーストアの使い方

デフォルトでは、パッチでデバッグ用のキーストアを生成しますが、環境変数を使用してキーストアの設定を上書きすることもできます:

```bash
KEYSTORE="./keystore/release.keystore" \
KEYSTORE_PASS="your_password" \
KEY_ALIAS="your_key_alias" \
./patch.sh
```

またはエクスポートしてください:

```bash
export KEYSTORE="./keystore/release.keystore"
export KEYSTORE_PASS="your_password"
export KEY_ALIAS="your_key_alias"
./patch.sh
```

キーエイリアスを見つけるには以下を実行:

```bash
keytool -list -keystore ./keystore/release.keystore
```

#### ビルドのリリースバリアント

GameHub Liteは、特定のデバイスのパフォーマンス最適化を可能にするために、複数のパッケージ名バリアントのリリースバージョンで配布しています。詳細は[GameHub Liteの異なるバージョン](#different-versions-of-gamehub-lite)を参照してください。

5つのバリアントすべてを一度にビルドするには`RELEASE=true`フラグを使用します:

```bash
RELEASE=true \
KEYSTORE="./keystore/release.keystore" \
KEYSTORE_PASS="your_password" \
KEY_ALIAS="your_key_alias" \
./patch.sh
```

`output/`ディレクトリに以下のAPKファイルが生成されます:

| バリアント    | ファイル名                                 | パッケージ名                |
| ---------- | ---------------------------------------- | --------------------------- |
| Base       | `GameHub-Lite-v{バージョン}.apk`            | `gamehub.lite`              |
| AnTuTu     | `GameHub-Lite-v{バージョン}-antutu.apk`     | `com.antutu.ABenchMark`     |
| Alt-AnTuTu | `GameHub-Lite-v{バージョン}-alt-antutu.apk` | `com.antutu.benchmark.full` |
| Ludashi    | `GameHub-Lite-v{バージョン}-ludashi.apk`    | `com.ludashi.aibench`       |
| PUBG       | `GameHub-Lite-v{バージョン}-pubg.apk`       | `com.tencent.ig`            |

バージョン番号は、ソースAPKから自動抽出されます。

## 仕組み

Patcherは複数のステップのプロセスを使用します:

1. **デコンパイル** - apktoolを使用して元のAPKをsmaliバイトコードにデコンパイルします
2. **削除** - テレメトリSDK、未使用のアセット、およびトラッキングライブラリを削除します
3. **パッチ** - 差分パッチを適用してsmaliコードを修正
4. **追加** - 新しいファイル(リソース、追加のsmali)をコピー
5. **リビルド** - apktoolを使用してAPKをリビルド
6. **署名** - インストール用のデバッグキーストアで署名

## パッチのコンテンツ

| カテゴリー      | 数 | 説明                                 |
| ------------- | ----- | -------------------------------------------- |
| 削除     | 3,385 | テレメトリSDK、トラッキングコード、未使用アセット |
| 追加     | 2,856 | 新しいリソース、変更したアセット、新機能 |
| 変更 | 223   | 動作変更のためのsmaliコードパッチ     |

### 削除したコンポーネント

- **ネイティブライブラリ**: libumeng-spy.so、libcrashsdk.so、libalicomphonenumberauthsdk_core.soなど
- **SDK**: Umeng Analytics、JPush、Firebase Analytics、Tencent login
- **アセット**: スプラッシュの動画、認証の動画、絵文字フォント (約30MBを節約)
- **権限**: 位置情報、連絡先、電話の状態、広告の追跡

### 追加される機能

- コミュニティCDNからのカスタムスプラッシュ/イントロ動画
- ローカルゲームIDのコピー機能
- オフラインモードの改善
- ニュースページとコミュニティAPIの連携

## 開発者向け

### パッチによる再生

Lite APKを改造してパッチを更新する場合は:

```bash
./generate-patches.sh [path/to/original.apk] [path/to/lite.apk]
```

以下を行う:

1. 両方のAPKファイルをデコンパイル
2. 変更されたファイルに対応する差分パッチを生成
3. 新しいファイルをパッチディレクトリにコピー
4. 削除リストと追加リストを作成

### パッチディレクトリの構造

```
patches/
├── files_to_delete.txt    # 削除するファイルのリスト
├── files_to_add.txt       # 追加するファイルのリスト
├── files_to_patch.txt     # 変更するファイルのリスト
├── diffs/                 # 統合する差分パッチ
│   ├── AndroidManifest.xml.patch
│   ├── smali/...
│   └── res/...
├── new_files/             # 追加する新しいファイル
│   ├── res/...
│   └── smali_classes10/...
└── stats.txt              # パッチの統計
```

### Modifying Patches

1. Decompile the Lite APK manually:

   ```bash
   apktool d apk/GameHub-Lite.apk -o work/lite
   ```

2. Make your changes to files in `work/lite/`

3. Rebuild and test:

   ```bash
   apktool b work/lite -o work/test.apk
   # Sign and install for testing
   ```

4. When satisfied, regenerate patches:
   ```bash
   ./generate-patches.sh
   ```

## Troubleshooting

### Patch fails to apply

If patches fail due to APK version mismatch:

- Ensure you're using GameHub 5.1.0 exactly
- Check the MD5 hash matches expected value
- Try regenerating patches with your APK version

### APK won't install

- Uninstall any existing GameHub Lite first
- Check if device is rooted - some patches may conflict (unlikely)

### Build errors

- Ensure apktool is version 2.8.0+ (`apktool --version`)
- Check Java version is 17+ (`java -version`)
- Try cleaning work directory: `rm -rf work/`

## Version Compatibility

| GameHub Version | Patcher Version | Status         |
| --------------- | --------------- | -------------- |
| 5.1.0           | 1.0             | Supported      |
| 5.3.3           | -               | in development |

## Alternative: ReVanced Patches (WORK IN PROGRESS)

An alternative ReVanced-based patching system is available in the `revanced/` directory. This approach uses the [ReVanced Patcher](https://github.com/ReVanced/revanced-patcher) framework for bytecode-level modifications.
This doesn't currently support all features of the Lite APK, but you are free to contribute patches for missing features and resolve existing issues.

```bash
cd revanced
./apply-patches.sh ../apk/GameHub-5.1.0.apk
```

See [revanced/README.md](revanced/README.md) for details.

### When to Use Which

| Approach                      | Best For                                                |
| ----------------------------- | ------------------------------------------------------- |
| **Diff-based** (`./patch.sh`) | Full control, complete SDK removal, simpler maintenance |
| **ReVanced** (`revanced/`)    | ReVanced Manager integration, portable JAR patches      |

## License

This project is for educational and personal privacy purposes only. The patches and tooling are provided as-is. GameHub is a product of its respective owners.

## Contributing

1. Fork the repository
2. Make your changes
3. Regenerate patches with `./generate-patches.sh`
4. Test the full patch cycle with `./patch.sh`
5. Submit a pull request

## Different versions of GameHub Lite

The different versions are identical. What we do is a very old android trick to gain extra performance on some devices.

**Antutu**

Some manufacturers “cheat” by setting the governor to performance when they detect the Antutu package name.

_Nerd explanation:_

The CPU governor essentially controls the CPU's frequency scaling. allowing it to operate at different clock speeds and voltages based on the system load. So making the CPU go fast for sustained usage, what is actually made for peak usage. This comes with a risk of overheating, but I don’t believe in this. Android does a well enough job of thermal management and makes it extremely hard for software to exceed what the hardware is capable of and damaging itself.

That said, it’s still extra heat. More heat == more bad. I just think it’s negligible, especially if your device has a fan.

**PUBG**

On a high level it’s the same as Antutu, but some slight differences that only benefit games. Think of network prioritization and touch input latency improvements. The manufacturers goal when they detect Antutu is **ALL THE POWER**. Benchmarks are relatively short and it makes them look better on comparison websites.

The goal for PUBG is more like **MORE POWER**, since the intention is often to have a game running for longer it has less aggressive changes.

**TLDR and summary:**

**Antutu** spoofing:

    •    Maximum CPU/GPU frequencies unlocked
    •    Aggressive performance governors
    •    Short-duration performance boost (benchmark workload)
    •    Thermal limits are less strict
    •    All cores available

**PUBG** and other games spoofing:

    •    Sustained gaming performance profiles
    •    GPU driver optimizations (Adreno/Mali game-specific paths)
    •    Frame pacing and scheduling improvements
    •    Reduced touch latency
    •    Network QoS prioritization
    •    Different thermal management (sustained vs burst)
    •    Qualcomm “Game Performance Mode”
    •    Sometimes enables features like frame-gen

**Ludashi** spoofing:

    •    Similar to Antutu but slightly less aggressive
    •    Longer sustained performance boost (multi-minute tests)
    •    Memory frequency optimization

## Support

If you have trouble running a game, please see if anyone shared a solution on [EmuReady](https://www.emuready.com) before you ask for help in the Discord server.

For support, discussion and development updates join the [EmuReady Discord server](https://discord.gg/CYhCzApXav).

---

## Related Projects

| Repository                                                                               | Description                                                                                                                                                      |
| ---------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [gamehub-lite](https://github.com/Producdevity/gamehub-lite)                             | Main project with pre-built APK releases and patch files                                                                                                         |
| [gamehub-lite-api](https://github.com/Producdevity/gamehub-lite-api)                     | Static JSON API hosting component manifests, configuration files, and mock responses that replace the original Chinese servers                                   |
| [gamehub-lite-worker](https://github.com/Producdevity/gamehub-lite-worker)               | Cloudflare Worker API proxy that handles token management, signature regeneration, privacy protection (IP hiding, fingerprint sanitization), and content routing |
| [gamehub-lite-news](https://github.com/Producdevity/gamehub-lite-news)                   | News aggregator that collects gaming news from RSS feeds and GitHub releases, transforms them into GameHub's API format                                          |
| [gamehub-lite-token-refresh](https://github.com/Producdevity/gamehub-lite-token-refresh) | Automated token refresher that uses Mail.tm OTP authentication to maintain valid GameHub tokens, runs every 4 hours via Cloudflare Cron                          |
