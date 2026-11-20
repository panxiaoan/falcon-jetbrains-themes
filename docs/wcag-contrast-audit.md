# WCAG 2.1 对比度核算对照表

> 适用范围：`src/main/resources/themes/` 下 **28** 个主题 JSON + **14** 个编辑器配色方案 XML  
> 判定依据：WCAG 2.1 成功准则 1.4.3（对比度最小值）与 1.4.11（非文本对比度）  
> 生成方式：脚本精确计算，非人工估算；本文档所有数值均可复现  
> 核算粒度：**(前景色, 背景色) 配对级** —— 同一前景色在不同底色上分别判定并分别给出建议值

## 一、判定标准与修复策略

| 判定线 | 含义 | 适用对象 |
|---|---|---|
| **4.5 : 1** | 正文文本 AA 级 | 代码正文、关键字、字符串、UI 前景文字 |
| **3 : 1** | 大字号 / 非文本组件 AA 级 | 注释等次要文本、图标、边框、下划线、状态条 |
| 7 : 1 | AAA 级 | 本次**不作为强制门槛**（与低饱和柔和风格冲突，见下） |

### 修复策略：柔和保色

主题设计原则是「颜色不刺眼」。本表的建议色值遵循以下算法，与该原则兼容：

1. **保持色相不变** —— 只调整明度（HSL 的 L），Hue 完全保留，色相族的视觉语义不丢失；
2. **收敛饱和度** —— 建议值饱和度上限 `S ≤ 0.62`，且在明度大幅偏移时进一步衰减（变暗防浑浊、变亮防荧光），确保调整后依然「不刺眼」；
3. **最小改动** —— 取满足目标对比度的**最小**明度偏移量，最大限度贴近原设计；
4. **分级达标** —— 注释等次要文本只要求 3:1（保留弱化层次），正文类要求 4.5:1；
5. **双向可选** —— 每条不达标配对同时给出「改前景」与「改背景」两条路径，由设计意图决定走哪条。

> **为什么这不违背设计原则**：对比度衡量的是「亮度差」，刺眼程度主要来自「高饱和 + 极端明暗差」。本策略把饱和度作为上限约束，只通过适度明度差来换取可读性，方向上与「降低亮度差带来的视觉冲击」并不冲突；且低对比度本身才是阅读疲劳的主因。

### 表中符号说明

| 标记 | 含义 |
|---|---|
| `✓` | 该方案可行，替换后达到目标对比度 |
| `✗` | 该方案数学上不可行（已达到亮度极值仍不足），需与其他手段组合 |
| `需改背景` | 仅调前景无法达标，必须同时调整对应背景色 |
| `✗ 无解` | 该 token 同时用于明暗差异极大的多个底色，单一色值无法同时满足，必须按使用点分别设定 |
| `ANSI 语义色` | 控制台 ANSI 固定色映射（该色值下**全部**属性均为 `CONSOLE_*`），不属于主题配色自由度，**不建议修改** |
| `ANSI 混用` | 该色值**同时**用于普通语法属性与 `CONSOLE_*` 控制台属性 —— 语法侧需改色，但改色会连带影响控制台配色，需人工确认 |
| `自带背景` | 该语法属性自带 `BACKGROUND`，需单独定值，不能全局替换 |
| `核心底色` | 该背景是编辑器 / 主面板底色，修改会影响全局观感，优先改前景 |
| `同时用作背景` | 该前景 token 在同一主题内也被当作背景色引用，替换时需同步复核 |

## 二、总览

| 类别 | 文件数 | 不达标条目 | 其中：全局统一替换即可解决 | 其中：需按使用点分别设定 | ANSI 豁免 | ANSI 混用 |
|---|---|---|---|---|---|---|
| 主题 JSON（theme.json） | 28 / 28 | 327 配对 | 183 前景 token | 30 前景 token | — | — |
| 编辑器方案 XML（单文件内） | 14 / 14 | 200 条 | 136 色值 | 26 色值 | 13 | 20 |
| 编辑器方案 XML（跨文件统一） | 14 | 58 色值 | 55 色值 | 3 色值 | — | — |
| 色盲变体（deuteranopia/protanopia） | — | 见第六章 | — | — | — | — |

> **关键结论**：全部 327 个不达标配对中，**仅调整前景色即可全部达标**（数学上均可通过保色相明度偏移达成，无需改背景）。
> 但其中 **30 个前景 token**（共 119 条配对）复用在了明暗差异极大的多个底色上（例如正文色同时用于浅底选中区与深底徽章），**单一色值无法同时满足**，必须按使用点分别设定 —— 这类项在各主题的「全局统一建议值」表中标为 `✗ 无统一值`，其逐使用点的取值见「逐配对明细」。
>
> **建议实施顺序**：先执行各主题的「全局统一建议值」（改动量最小、覆盖大多数场景），再对 `✗ 无统一值` 的 token 按「逐配对明细」逐个处理 —— 这些多为本主题内的强调色 / 徽章底色，改动视觉影响可控。
>
> **ANSI 处理原则**：`CONSOLE_*_OUTPUT` 是终端 ANSI 16 色的固定映射。仅当某个色值下的属性**全部**为 `CONSOLE_*` 时才按「语义色」豁免；若同一色值**同时**被普通语法属性与 `CONSOLE_*` 属性共用（共 20 条），则语法侧仍需改色才能达标，但改色会连带影响控制台配色，表中已逐条标注 `ANSI 混用`，需人工确认后再改。
>
> **配套文件**：`docs/wcag-contrast-audit.csv` 为同一数据的 CSV 版本（含全部 527 行），便于排序筛选与批量改色。

## 三、主题 JSON 不达标项对照表

每个主题按三张表给出结果：

1. **全局统一建议值** —— 一次性替换该前景 token 的值，覆盖它在**本主题内全部底色**（含原本已达标的底色）；
2. **背景侧诊断** —— 列出被多个前景色共用的背景 token，提示改动连带影响；
3. **逐配对明细** —— 每个不达标的「前景 × 背景」组合独立判定并给出建议值。

> `覆盖底色数 N` = 该前景 token 在本主题内被用在 N 个不同底色上。替换 token 值会**同时影响这 N 处**，因此请以「全局统一建议值」为准；标注 `✗ 无统一值` 的 token 说明单一色值无法同时满足这 N 个底色，需按「逐配对明细」在使用点内联单独色值。

### dark/dark-blue

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `primaryForeground` | `#BBBBBB` | — | 2.62 | 8 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `hoverBackground` | `#4D5C74` | — | 3.10 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `secondaryBackground` | `#19273D` | `#F8F8F8` | 4.51 | 1 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#9699A4` | 4.51 | 1 | ✓ 可一次替换 |
| `underlineColor` | `#28B366` | `#30C773` | 4.56 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `underlineColor` | `#28B366` | `#233348`、`#4D5C74`、`#BBBBBB` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `selectionBackground` | `#4D5C74` | `#4D5C74`、`#BBBBBB` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `primaryBackground` | `#233348` | `#727684`、`#BBBBBB` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |
| `tabColor` | `#344359` | `#28B366`、`#BBBBBB` | 2 | ✓ 可一次替换 |  |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#4D5C74` | `selectionBackground` = `#4D5C74` | 1.00 | 4.5 | `#CDD3DD` → 4.50 ✓ | `#CDD3DD` → 4.50 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#4D5C74` | `underlineColor` = `#28B366` | 2.49 | 4.5 | `#2E3541` → 4.54 ✓ | `#90E5B6` → 4.54 ✓ ⚠该底色还被别处用作前景 ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#BBBBBB` | `#5da3f4`（内联） = `#5DA3F4` | 1.37 | 4.5 | `#373737` → 4.54 ✓ | `#234C7C` → 4.56 ✓ ⚠改动大 | UI 配对 Plugins.Button.updateForeground↔updateBackground | 改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#BBBBBB` | `underlineColor` = `#28B366` | 1.42 | 4.5 | `#353535` → 4.52 ✓ | `#1B5535` → 4.56 ✓ ⚠该底色还被别处用作前景 | UI 配对 SearchEverywhere.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `#8d6b81`（内联） = `#8D6B81` | 2.40 | 4.5 | `#FDFDFD` → 4.53 ✓ | `#594653` → 4.51 ✓ | UI 配对 Bookmark.MnemonicCurrent.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `#786299`（内联） = `#786299` | 2.73 | 4.5 | `#EEEEEE` → 4.52 ✓ | `#534568` → 4.52 ✓ | UI 配对 Bookmark.MnemonicAssigned.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `selectionBackground` = `#4D5C74` | 3.53 | 4.5 | `#D3D3D3` → 4.53 ✓ | `#414C5E` → 4.52 ✓ | 正文 primaryForeground × selectionBackground |  |
| `primaryForeground` = `#BBBBBB` | `selectionInactiveBackground` = `#4D5C74` | 3.53 | 4.5 | `#D3D3D3` → 4.53 ✓ | `#414C5E` → 4.52 ✓ | 正文 primaryForeground × selectionInactiveBackground |  |
| `primaryForeground` = `#BBBBBB` | `tabHoverBackground` = `#4D5C74` | 3.53 | 4.5 | `#D3D3D3` → 4.53 ✓ | `#414C5E` → 4.52 ✓ | UI 配对 MainWindow.Tab.hoverForeground↔hoverBackground |  |
| `#727684`（内联） = `#727684` | `primaryBackground` = `#233348` | 2.83 | 4.5 | `#9699A4` → 4.51 ✓ | `#050505` → 4.50 ✓ ⚠该底色还被别处用作前景 | UI 配对 TrialWidget.Active.foreground↔background |  |
| `secondaryBackground` = `#19273D` | `iconHoverBackground` = `#0B843E` | 3.13 | 4.5 | `#F8F8F8` → 4.51 ✓ | `#2AA15C` → 4.54 ✓ | UI 配对 Plugins.Button.installForeground↔installBackground | 该 token 同时用作背景，替换需同步复核 |
| `underlineColor` = `#28B366` | `tabColor` = `#344359` | 3.69 | 4.5 | `#30C773` → 4.56 ✓ | `#2B3544` → 4.56 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground | 该 token 同时用作背景，替换需同步复核 |

### dark/dark-coffee

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `secondaryBackground` | `#201315` | — | 2.64 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `primaryForeground` | `#BBBBBB` | — | 4.01 | 8 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `hoverBackground` | `#6E4552` | — | 4.01 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `primaryBackground` | `#2C2122` | `#1A1515` | 4.51 | 1 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#868A96` | 4.52 | 1 | ✓ 可一次替换 |
| `underlineColor` | `#A96F79` | `#BF949B` | 4.53 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `underlineColor` | `#A96F79` | `#2C2122`、`#6E4552`、`#BBBBBB` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `selectionBackground` | `#6E4552` | `#6E4552`、`#BBBBBB` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `primaryBackground` | `#2C2122` | `#727684`、`#BBBBBB` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |
| `tabColor` | `#413334` | `#A96F79`、`#BBBBBB` | 2 | ✓ 可一次替换 |  |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#6E4552` | `selectionBackground` = `#6E4552` | 1.00 | 4.5 | `#D5BDC5` → 4.52 ✓ | `#D5BDC5` → 4.52 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#6E4552` | `underlineColor` = `#A96F79` | 1.99 | 4.5 | `#1C1316` → 4.54 ✓ | `#D6BDC1` → 4.52 ✓ ⚠该底色还被别处用作前景 | UI 配对 ToggleButton.onForeground↔onBackground |  |
| `primaryForeground` = `#BBBBBB` | `underlineColor` = `#A96F79` | 2.09 | 4.5 | `#161616` → 4.51 ✓ | `#674148` → 4.51 ✓ ⚠该底色还被别处用作前景 | UI 配对 SearchEverywhere.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `#917800`（内联） = `#917800` | 2.24 | 4.5 | `#0D0D0D` → 4.53 ✓ | `#554B1B` → 4.54 ✓ | UI 配对 Plugins.Button.updateForeground↔updateBackground |  |
| `primaryForeground` = `#BBBBBB` | `#8d6b81`（内联） = `#8D6B81` | 2.40 | 4.5 | `#FDFDFD` → 4.53 ✓ | `#594653` → 4.51 ✓ | UI 配对 Bookmark.MnemonicCurrent.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `#786299`（内联） = `#786299` | 2.73 | 4.5 | `#EEEEEE` → 4.52 ✓ | `#534568` → 4.52 ✓ | UI 配对 Bookmark.MnemonicAssigned.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `selectionBackground` = `#6E4552` | 4.15 | 4.5 | `#C3C3C3` → 4.52 ✓ | `#64424D` → 4.50 ✓ | 正文 primaryForeground × selectionBackground |  |
| `primaryForeground` = `#BBBBBB` | `selectionInactiveBackground` = `#6E4552` | 4.15 | 4.5 | `#C3C3C3` → 4.52 ✓ | `#64424D` → 4.50 ✓ | 正文 primaryForeground × selectionInactiveBackground |  |
| `primaryForeground` = `#BBBBBB` | `tabHoverBackground` = `#6E4552` | 4.15 | 4.5 | `#C3C3C3` → 4.52 ✓ | `#64424D` → 4.50 ✓ | UI 配对 MainWindow.Tab.hoverForeground↔hoverBackground |  |
| `secondaryBackground` = `#201315` | `iconHoverBackground` = `#6E4552` | 2.26 | 4.5 | `#D6BDC1` → 4.52 ✓ | `#A67081` → 4.51 ✓ | UI 配对 Plugins.Button.installForeground↔installBackground | 该 token 同时用作背景，替换需同步复核 |
| `underlineColor` = `#A96F79` | `tabColor` = `#413334` | 3.00 | 4.5 | `#BF949B` → 4.53 ✓ | `#1A1516` → 4.50 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground | 该 token 同时用作背景，替换需同步复核 |
| `#727684`（内联） = `#727684` | `primaryBackground` = `#2C2122` | 3.44 | 4.5 | `#868A96` → 4.52 ✓ | `#050505` → 4.50 ✓ ⚠该底色还被别处用作前景 | UI 配对 TrialWidget.Active.foreground↔background |  |
| `primaryBackground` = `#2C2122` | `underlineColor` = `#A96F79` | 3.89 | 4.5 | `#1A1515` → 4.51 ✓ | `#B17C85` → 4.52 ✓ ⚠该底色还被别处用作前景 | UI 配对 Counter.foreground↔background | 该 token 同时用作背景，替换需同步复核 |

### dark/dark-darcula

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `primaryForeground` | `#BBBBBB` | — | 3.81 | 6 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `hoverBackground` | `#666A6C` | — | 3.84 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `secondaryBackground` | `#12191F` | `#E4EAEE` | 4.50 | 1 | ✓ 可一次替换 |
| `primaryBackground` | `#3C3F41` | `#1A1B1C` | 4.52 | 1 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#A6A9B2` | 4.52 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `underlineColor` | `#9876AA` | `#3C3F41`、`#666A6C`、`#BBBBBB` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `selectionBackground` | `#666A6C` | `#666A6C`、`#BBBBBB` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `primaryBackground` | `#3C3F41` | `#727684`、`#BBBBBB` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#666A6C` | `selectionBackground` = `#666A6C` | 1.00 | 4.5 | `#E9E9EA` → 4.50 ✓ | `#E9E9EA` → 4.50 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#666A6C` | `underlineColor` = `#9876AA` | 1.43 | 4.5 | `#1A1B1B` → 4.53 ✓ | `#EDE8F0` → 4.53 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#BBBBBB` | `underlineColor` = `#9876AA` | 1.99 | 4.5 | `#1B1B1B` → 4.52 ✓ | `#5A4266` → 4.55 ✓ | UI 配对 SearchEverywhere.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `#8d6b81`（内联） = `#8D6B81` | 2.40 | 4.5 | `#FDFDFD` → 4.53 ✓ | `#594653` → 4.51 ✓ | UI 配对 Bookmark.MnemonicCurrent.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `#786299`（内联） = `#786299` | 2.73 | 4.5 | `#EEEEEE` → 4.52 ✓ | `#534568` → 4.52 ✓ | UI 配对 Bookmark.MnemonicAssigned.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `selectionBackground` = `#666A6C` | 2.85 | 4.5 | `#E9E9E9` → 4.50 ✓ | `#494C4D` → 4.51 ✓ | 正文 primaryForeground × selectionBackground |  |
| `primaryForeground` = `#BBBBBB` | `selectionInactiveBackground` = `#666A6C` | 2.85 | 4.5 | `#E9E9E9` → 4.50 ✓ | `#494C4D` → 4.51 ✓ | 正文 primaryForeground × selectionInactiveBackground |  |
| `primaryForeground` = `#BBBBBB` | `tabHoverBackground` = `#666A6C` | 2.85 | 4.5 | `#E9E9E9` → 4.50 ✓ | `#494C4D` → 4.51 ✓ | UI 配对 MainWindow.Tab.hoverForeground↔hoverBackground |  |
| `primaryForeground` = `#BBBBBB` | `#666a6c`（内联） = `#666A6C` | 2.85 | 4.5 | `#E9E9E9` → 4.50 ✓ | `#494C4D` → 4.51 ✓ | UI 配对 Plugins.Button.updateForeground↔updateBackground |  |
| `primaryForeground` = `#BBBBBB` | `hoverBackground` = `#666A6C` | 2.85 | 4.5 | `#E9E9E9` → 4.50 ✓ | `#494C4D` → 4.51 ✓ ⚠该底色还被别处用作前景 | UI 配对 Plugins.Tab.selectedForeground↔selectedBackground |  |
| `#727684`（内联） = `#727684` | `primaryBackground` = `#3C3F41` | 2.34 | 4.5 | `#A6A9B2` → 4.52 ✓ | `#050505` → 4.50 ✓ ⚠该底色还被别处用作前景 | UI 配对 TrialWidget.Active.foreground↔background |  |
| `primaryBackground` = `#3C3F41` | `underlineColor` = `#9876AA` | 2.78 | 4.5 | `#1A1B1C` → 4.52 ✓ | `#B8A1C4` → 4.51 ✓ | UI 配对 Counter.foreground↔background | 该 token 同时用作背景，替换需同步复核 |
| `secondaryBackground` = `#12191F` | `iconHoverBackground` = `#666A6C` | 3.24 | 4.5 | `#E4EAEE` → 4.50 ✓ | `#7D8184` → 4.51 ✓ | UI 配对 Plugins.Button.installForeground↔installBackground | 该 token 同时用作背景，替换需同步复核 |

### dark/dark-green

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `primaryForeground` | `#BBBBBB` | — | 2.72 | 7 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `hoverBackground` | `#305862` | — | 2.72 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `underlineColor` | `#28B366` | `#2EB96C` | 4.50 | 1 | ✓ 可一次替换 |
| `secondaryBackground` | `#19273D` | `#F8F8F8` | 4.51 | 1 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#8D909C` | 4.51 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `underlineColor` | `#28B366` | `#002F38`、`#305862`、`#BBBBBB` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `selectionBackground` | `#305862` | `#305862`、`#BBBBBB` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `primaryBackground` | `#002F38` | `#727684`、`#BBBBBB` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#305862` | `selectionBackground` = `#305862` | 1.00 | 4.5 | `#A8CBD4` → 4.51 ✓ | `#A8CBD4` → 4.51 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#305862` | `underlineColor` = `#28B366` | 2.87 | 4.5 | `#22383D` → 4.54 ✓ | `#65DB9A` → 4.50 ✓ ⚠该底色还被别处用作前景 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核 |
| `primaryForeground` = `#BBBBBB` | `underlineColor` = `#28B366` | 1.42 | 4.5 | `#353535` → 4.52 ✓ | `#1B5535` → 4.56 ✓ ⚠该底色还被别处用作前景 | UI 配对 SearchEverywhere.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `#00A6A6`（内联） = `#00A6A6` | 1.56 | 4.5 | `#2E2E2E` → 4.53 ✓ | `#1A5353` → 4.55 ✓ | UI 配对 Plugins.Button.updateForeground↔updateBackground |  |
| `primaryForeground` = `#BBBBBB` | `#8d6b81`（内联） = `#8D6B81` | 2.40 | 4.5 | `#FDFDFD` → 4.53 ✓ | `#594653` → 4.51 ✓ | UI 配对 Bookmark.MnemonicCurrent.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `#786299`（内联） = `#786299` | 2.73 | 4.5 | `#EEEEEE` → 4.52 ✓ | `#534568` → 4.52 ✓ | UI 配对 Bookmark.MnemonicAssigned.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `selectionBackground` = `#305862` | 4.05 | 4.5 | `#C5C5C5` → 4.51 ✓ | `#305059` → 4.52 ✓ | 正文 primaryForeground × selectionBackground |  |
| `primaryForeground` = `#BBBBBB` | `selectionInactiveBackground` = `#305862` | 4.05 | 4.5 | `#C5C5C5` → 4.51 ✓ | `#305059` → 4.52 ✓ | 正文 primaryForeground × selectionInactiveBackground |  |
| `primaryForeground` = `#BBBBBB` | `tabHoverBackground` = `#305862` | 4.05 | 4.5 | `#C5C5C5` → 4.51 ✓ | `#305059` → 4.52 ✓ | UI 配对 MainWindow.Tab.hoverForeground↔hoverBackground |  |
| `primaryForeground` = `#BBBBBB` | `hoverBackground` = `#305862` | 4.05 | 4.5 | `#C5C5C5` → 4.51 ✓ | `#305059` → 4.52 ✓ ⚠该底色还被别处用作前景 | UI 配对 Plugins.Tab.selectedForeground↔selectedBackground |  |
| `secondaryBackground` = `#19273D` | `iconHoverBackground` = `#0B843E` | 3.13 | 4.5 | `#F8F8F8` → 4.51 ✓ | `#2AA15C` → 4.54 ✓ | UI 配对 Plugins.Button.installForeground↔installBackground | 该 token 同时用作背景，替换需同步复核 |
| `#727684`（内联） = `#727684` | `primaryBackground` = `#002F38` | 3.17 | 4.5 | `#8D909C` → 4.51 ✓ | `#050505` → 4.50 ✓ ⚠该底色还被别处用作前景 | UI 配对 TrialWidget.Active.foreground↔background |  |
| `underlineColor` = `#28B366` | `tabColor` = `#153F48` | 4.21 | 4.5 | `#2EB96C` → 4.50 ✓ | `#183940` → 4.55 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground | 该 token 同时用作背景，替换需同步复核 |

### dark/dark-violet

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `primaryForeground` | `#BBBBBB` | — | 3.81 | 6 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `hoverBackground` | `#544E6B` | — | 3.81 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `underlineColor` | `#9876AA` | `#BBA5C7` | 4.52 | 1 | ✓ 可一次替换 |
| `primaryBackground` | `#2B2640` | `#1C1926` | 4.53 | 1 | ✓ 可一次替换 |
| `secondaryBackground` | `#0F0B1F` | `#C8C0E6` | 4.53 | 1 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#8C909C` | 4.53 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `underlineColor` | `#9876AA` | `#2B2640`、`#544E6B`、`#BBBBBB` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `selectionBackground` | `#544E6B` | `#544E6B`、`#BBBBBB` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `primaryBackground` | `#2B2640` | `#727684`、`#BBBBBB` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#544E6B` | `selectionBackground` = `#544E6B` | 1.00 | 4.5 | `#C6C3D2` → 4.53 ✓ | `#C6C3D2` → 4.53 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#544E6B` | `underlineColor` = `#9876AA` | 2.06 | 4.5 | `#1C1A22` → 4.51 ✓ | `#CFC0D7` → 4.54 ✓ ⚠该底色还被别处用作前景 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核 |
| `primaryForeground` = `#BBBBBB` | `underlineColor` = `#9876AA` | 1.99 | 4.5 | `#1B1B1B` → 4.52 ✓ | `#5A4266` → 4.55 ✓ ⚠该底色还被别处用作前景 | UI 配对 SearchEverywhere.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `#8d6b81`（内联） = `#8D6B81` | 2.40 | 4.5 | `#FDFDFD` → 4.53 ✓ | `#594653` → 4.51 ✓ | UI 配对 Bookmark.MnemonicCurrent.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `#786299`（内联） = `#786299` | 2.73 | 4.5 | `#EEEEEE` → 4.52 ✓ | `#534568` → 4.52 ✓ | UI 配对 Bookmark.MnemonicAssigned.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `selectionBackground` = `#544E6B` | 4.08 | 4.5 | `#C5C5C5` → 4.54 ✓ | `#4D4860` → 4.54 ✓ | 正文 primaryForeground × selectionBackground |  |
| `primaryForeground` = `#BBBBBB` | `selectionInactiveBackground` = `#544E6B` | 4.08 | 4.5 | `#C5C5C5` → 4.54 ✓ | `#4D4860` → 4.54 ✓ | 正文 primaryForeground × selectionInactiveBackground |  |
| `primaryForeground` = `#BBBBBB` | `tabHoverBackground` = `#544E6B` | 4.08 | 4.5 | `#C5C5C5` → 4.54 ✓ | `#4D4860` → 4.54 ✓ | UI 配对 MainWindow.Tab.hoverForeground↔hoverBackground |  |
| `primaryForeground` = `#BBBBBB` | `#544e6b`（内联） = `#544E6B` | 4.08 | 4.5 | `#C5C5C5` → 4.54 ✓ | `#4D4860` → 4.54 ✓ | UI 配对 Plugins.Button.updateForeground↔updateBackground |  |
| `primaryForeground` = `#BBBBBB` | `hoverBackground` = `#544E6B` | 4.08 | 4.5 | `#C5C5C5` → 4.54 ✓ | `#4D4860` → 4.54 ✓ ⚠该底色还被别处用作前景 | UI 配对 Plugins.Tab.selectedForeground↔selectedBackground |  |
| `secondaryBackground` = `#0F0B1F` | `iconHoverBackground` = `#544E6B` | 2.46 | 4.5 | `#C8C0E6` → 4.53 ✓ | `#7E769B` → 4.55 ✓ | UI 配对 Plugins.Button.installForeground↔installBackground | 该 token 同时用作背景，替换需同步复核 |
| `underlineColor` = `#9876AA` | `tabColor` = `#433D5C` | 2.67 | 4.5 | `#BBA5C7` → 4.52 ✓ | `#1C1A24` → 4.50 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground | 该 token 同时用作背景，替换需同步复核 |
| `#727684`（内联） = `#727684` | `primaryBackground` = `#2B2640` | 3.19 | 4.5 | `#8C909C` → 4.53 ✓ | `#050505` → 4.50 ✓ ⚠该底色还被别处用作前景 | UI 配对 TrialWidget.Active.foreground↔background |  |
| `primaryBackground` = `#2B2640` | `underlineColor` = `#9876AA` | 3.79 | 4.5 | `#1C1926` → 4.53 ✓ | `#A385B3` → 4.51 ✓ ⚠该底色还被别处用作前景 | UI 配对 Counter.foreground↔background | 该 token 同时用作背景，替换需同步复核 |

### dark/dark

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `primaryForeground` | `#BBBBBB` | — | 3.81 | 6 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `hoverBackground` | `#515260` | — | 3.81 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `primaryBackground` | `#282A36` | `#1A1B21` | 4.50 | 1 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#8D919D` | 4.52 | 1 | ✓ 可一次替换 |
| `secondaryBackground` | `#19273D` | `#B8C8E0` | 4.54 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `underlineColor` | `#9876AA` | `#282A36`、`#515260`、`#BBBBBB` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `selectionBackground` | `#515260` | `#515260`、`#BBBBBB` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `primaryBackground` | `#282A36` | `#727684`、`#BBBBBB` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#515260` | `selectionBackground` = `#515260` | 1.00 | 4.5 | `#C5C6CE` → 4.53 ✓ | `#C5C6CE` → 4.53 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#515260` | `underlineColor` = `#9876AA` | 2.02 | 4.5 | `#1B1B1F` → 4.50 ✓ | `#D0C2D8` → 4.55 ✓ | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核 |
| `secondaryBackground` = `#19273D` | `iconHoverBackground` = `#515260` | 1.95 | 4.5 | `#B8C8E0` → 4.54 ✓ | `#8B8C9D` → 4.53 ✓ | UI 配对 Plugins.Button.installForeground↔installBackground | 该 token 同时用作背景，替换需同步复核 |
| `primaryForeground` = `#BBBBBB` | `underlineColor` = `#9876AA` | 1.99 | 4.5 | `#1B1B1B` → 4.52 ✓ | `#5A4266` → 4.55 ✓ | UI 配对 SearchEverywhere.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `#8d6b81`（内联） = `#8D6B81` | 2.40 | 4.5 | `#FDFDFD` → 4.53 ✓ | `#594653` → 4.51 ✓ | UI 配对 Bookmark.MnemonicCurrent.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `#786299`（内联） = `#786299` | 2.73 | 4.5 | `#EEEEEE` → 4.52 ✓ | `#534568` → 4.52 ✓ | UI 配对 Bookmark.MnemonicAssigned.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `selectionBackground` = `#515260` | 4.01 | 4.5 | `#C6C6C6` → 4.51 ✓ | `#4A4A56` → 4.55 ✓ | 正文 primaryForeground × selectionBackground |  |
| `primaryForeground` = `#BBBBBB` | `selectionInactiveBackground` = `#515260` | 4.01 | 4.5 | `#C6C6C6` → 4.51 ✓ | `#4A4A56` → 4.55 ✓ | 正文 primaryForeground × selectionInactiveBackground |  |
| `primaryForeground` = `#BBBBBB` | `tabHoverBackground` = `#515260` | 4.01 | 4.5 | `#C6C6C6` → 4.51 ✓ | `#4A4A56` → 4.55 ✓ | UI 配对 MainWindow.Tab.hoverForeground↔hoverBackground |  |
| `primaryForeground` = `#BBBBBB` | `#515260`（内联） = `#515260` | 4.01 | 4.5 | `#C6C6C6` → 4.51 ✓ | `#4A4A56` → 4.55 ✓ | UI 配对 Plugins.Button.updateForeground↔updateBackground |  |
| `primaryForeground` = `#BBBBBB` | `hoverBackground` = `#515260` | 4.01 | 4.5 | `#C6C6C6` → 4.51 ✓ | `#4A4A56` → 4.55 ✓ ⚠该底色还被别处用作前景 | UI 配对 Plugins.Tab.selectedForeground↔selectedBackground |  |
| `#727684`（内联） = `#727684` | `primaryBackground` = `#282A36` | 3.15 | 4.5 | `#8D919D` → 4.52 ✓ | `#050505` → 4.50 ✓ ⚠该底色还被别处用作前景 | UI 配对 TrialWidget.Active.foreground↔background |  |
| `primaryBackground` = `#282A36` | `underlineColor` = `#9876AA` | 3.73 | 4.5 | `#1A1B21` → 4.50 ✓ | `#A486B4` → 4.51 ✓ | UI 配对 Counter.foreground↔background | 该 token 同时用作背景，替换需同步复核 |

### islands-dark/islands-dark-blue

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `primaryForeground` | `#BBBBBB` | — | 2.62 | 8 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `borderColor` | `#344359` | `#778EB0` | 3.00 | 1 | ✓ 可一次替换 |
| `lineColor` | `#5F6E86` | `#7F8EA5` | 3.02 | 1 | ✓ 可一次替换 |
| `accentColor` | `#9876AA` | `#A182B1` | 3.03 | 1 | ✓ 可一次替换 |
| `hoverBackground` | `#4D5C74` | — | 3.10 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `secondaryBackground` | `#19273D` | `#F8F8F8` | 4.51 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `underlineColor` | `#28B366` | `#233348`、`#4D5C74`、`#BBBBBB` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `mainBackground` | `#344359` | `#28B366`、`#344359`、`#5F6E86`、`#9876AA`、`#B4B8BF`、`#BBBBBB` | 6 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `selectionBackground` | `#4D5C74` | `#4D5C74`、`#BBBBBB` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `primaryBackground` | `#233348` | `#BBBBBB`、`#C6DBEE` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `borderColor` = `#344359` | `mainBackground` = `#344359` | 1.00 | 3.0 | `#778EB0` → 3.00 ✓ | `#778EB0` → 3.00 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#4D5C74` | `selectionBackground` = `#4D5C74` | 1.00 | 4.5 | `#CDD3DD` → 4.50 ✓ | `#CDD3DD` → 4.50 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#4D5C74` | `underlineColor` = `#28B366` | 2.49 | 4.5 | `#2E3541` → 4.54 ✓ | `#90E5B6` → 4.54 ✓ ⚠该底色还被别处用作前景 ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#BBBBBB` | `#5da3f4`（内联） = `#5DA3F4` | 1.37 | 4.5 | `#373737` → 4.54 ✓ | `#234C7C` → 4.56 ✓ ⚠改动大 | UI 配对 Plugins.Button.updateForeground↔updateBackground | 改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#BBBBBB` | `underlineColor` = `#28B366` | 1.42 | 4.5 | `#353535` → 4.52 ✓ | `#1B5535` → 4.56 ✓ ⚠该底色还被别处用作前景 | UI 配对 SearchEverywhere.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `#8d6b81`（内联） = `#8D6B81` | 2.40 | 4.5 | `#FDFDFD` → 4.53 ✓ | `#594653` → 4.51 ✓ | UI 配对 Bookmark.MnemonicCurrent.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `#786299`（内联） = `#786299` | 2.73 | 4.5 | `#EEEEEE` → 4.52 ✓ | `#534568` → 4.52 ✓ | UI 配对 Bookmark.MnemonicAssigned.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `selectionBackground` = `#4D5C74` | 3.53 | 4.5 | `#D3D3D3` → 4.53 ✓ | `#414C5E` → 4.52 ✓ | 正文 primaryForeground × selectionBackground |  |
| `primaryForeground` = `#BBBBBB` | `selectionInactiveBackground` = `#4D5C74` | 3.53 | 4.5 | `#D3D3D3` → 4.53 ✓ | `#414C5E` → 4.52 ✓ | 正文 primaryForeground × selectionInactiveBackground |  |
| `primaryForeground` = `#BBBBBB` | `tabColor` = `#4D5C74` | 3.53 | 4.5 | `#D3D3D3` → 4.53 ✓ | `#414C5E` → 4.52 ✓ | UI 配对 Plugins.Tab.selectedForeground↔selectedBackground |  |
| `lineColor` = `#5F6E86` | `mainBackground` = `#344359` | 1.94 | 3.0 | `#7F8EA5` → 3.02 ✓ | `#1E242E` → 3.01 ✓ | UI线条 lineColor |  |
| `accentColor` = `#9876AA` | `mainBackground` = `#344359` | 2.63 | 3.0 | `#A182B1` → 3.03 ✓ | `#2F3A4A` → 3.02 ✓ | UI强调 accentColor |  |
| `secondaryBackground` = `#19273D` | `iconHoverBackground` = `#0B843E` | 3.13 | 4.5 | `#F8F8F8` → 4.51 ✓ | `#2AA15C` → 4.54 ✓ | UI 配对 Plugins.Button.installForeground↔installBackground | 该 token 同时用作背景，替换需同步复核 |

### islands-dark/islands-dark-coffee

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `secondaryBackground` | `#201315` | — | 2.64 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `borderColor` | `#413334` | `#95787A` | 3.00 | 1 | ✓ 可一次替换 |
| `lineColor` | `#6E4552` | `#A67081` | 3.01 | 1 | ✓ 可一次替换 |
| `iconBackground` | `#A96F79` | `#AA707A` | 3.04 | 1 | ✓ 可一次替换 |
| `underlineColor` | `#A96F79` | `#B17C85` | 3.48 | 2 | ✓ 可一次替换 |
| `primaryForeground` | `#BBBBBB` | — | 4.01 | 8 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `hoverBackground` | `#6E4552` | — | 4.01 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `primaryBackground` | `#2C2122` | `#1A1515` | 4.51 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `mainBackground` | `#413334` | `#413334`、`#6E4552`、`#A96F79`、`#B4B8BF`、`#BBBBBB`、`#DBAB4A` | 6 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `underlineColor` | `#A96F79` | `#2C2122`、`#6E4552`、`#BBBBBB` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `selectionBackground` | `#6E4552` | `#6E4552`、`#BBBBBB` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `primaryBackground` | `#2C2122` | `#A96F79`、`#BBBBBB` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `borderColor` = `#413334` | `mainBackground` = `#413334` | 1.00 | 3.0 | `#95787A` → 3.00 ✓ | `#95787A` → 3.00 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#6E4552` | `selectionBackground` = `#6E4552` | 1.00 | 4.5 | `#D5BDC5` → 4.52 ✓ | `#D5BDC5` → 4.52 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#6E4552` | `underlineColor` = `#A96F79` | 1.99 | 4.5 | `#1C1316` → 4.54 ✓ | `#D6BDC1` → 4.52 ✓ ⚠该底色还被别处用作前景 | UI 配对 ToggleButton.onForeground↔onBackground |  |
| `lineColor` = `#6E4552` | `mainBackground` = `#413334` | 1.51 | 3.0 | `#A67081` → 3.01 ✓ | `#AF9A9B` → 3.00 ✓ ⚠改动大 | UI线条 lineColor | 改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#BBBBBB` | `underlineColor` = `#A96F79` | 2.09 | 4.5 | `#161616` → 4.51 ✓ | `#674148` → 4.51 ✓ ⚠该底色还被别处用作前景 | UI 配对 SearchEverywhere.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `#917800`（内联） = `#917800` | 2.24 | 4.5 | `#0D0D0D` → 4.53 ✓ | `#554B1B` → 4.54 ✓ | UI 配对 Plugins.Button.updateForeground↔updateBackground |  |
| `primaryForeground` = `#BBBBBB` | `#8d6b81`（内联） = `#8D6B81` | 2.40 | 4.5 | `#FDFDFD` → 4.53 ✓ | `#594653` → 4.51 ✓ | UI 配对 Bookmark.MnemonicCurrent.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `#786299`（内联） = `#786299` | 2.73 | 4.5 | `#EEEEEE` → 4.52 ✓ | `#534568` → 4.52 ✓ | UI 配对 Bookmark.MnemonicAssigned.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `selectionBackground` = `#6E4552` | 4.15 | 4.5 | `#C3C3C3` → 4.52 ✓ | `#64424D` → 4.50 ✓ | 正文 primaryForeground × selectionBackground |  |
| `primaryForeground` = `#BBBBBB` | `selectionInactiveBackground` = `#6E4552` | 4.15 | 4.5 | `#C3C3C3` → 4.52 ✓ | `#64424D` → 4.50 ✓ | 正文 primaryForeground × selectionInactiveBackground |  |
| `primaryForeground` = `#BBBBBB` | `tabColor` = `#6E4552` | 4.15 | 4.5 | `#C3C3C3` → 4.52 ✓ | `#64424D` → 4.50 ✓ | UI 配对 Plugins.Tab.selectedForeground↔selectedBackground |  |
| `secondaryBackground` = `#201315` | `iconHoverBackground` = `#6E4552` | 2.26 | 4.5 | `#D6BDC1` → 4.52 ✓ | `#A67081` → 4.51 ✓ | UI 配对 Plugins.Button.installForeground↔installBackground | 该 token 同时用作背景，替换需同步复核 |
| `underlineColor` = `#A96F79` | `mainBackground` = `#413334` | 3.00 | 3.0 | `#B17C85` → 3.48 ✓ | `#3F3334` → 3.02 ✓ | UI强调 underlineColor | 该 token 同时用作背景，替换需同步复核 |
| `underlineColor` = `#A96F79` | `primaryBackground` = `#2C2122` | 3.89 | 4.5 | `#B17C85` → 4.52 ✓ | `#1A1515` → 4.51 ✓ ⚠该底色还被别处用作前景 | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground | 该 token 同时用作背景，替换需同步复核 |
| `iconBackground` = `#A96F79` | `mainBackground` = `#413334` | 3.00 | 3.0 | `#AA707A` → 3.04 ✓ | `#3F3334` → 3.02 ✓ | UI图标 iconBackground |  |
| `primaryBackground` = `#2C2122` | `underlineColor` = `#A96F79` | 3.89 | 4.5 | `#1A1515` → 4.51 ✓ | `#B17C85` → 4.52 ✓ ⚠该底色还被别处用作前景 | UI 配对 Counter.foreground↔background | 该 token 同时用作背景，替换需同步复核 |

### islands-dark/islands-dark-darcula

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `borderColor` | `#4C5052` | `#999EA1` | 3.01 | 1 | ✓ 可一次替换 |
| `accentColor` | `#9876AA` | `#AF94BD` | 3.02 | 1 | ✓ 可一次替换 |
| `underlineColor` | `#9876AA` | `#AF94BD` | 3.02 | 1 | ✓ 可一次替换 |
| `iconBackground` | `#9876AA` | `#AF94BD` | 3.02 | 1 | ✓ 可一次替换 |
| `lineColor` | `#666A6C` | `#9A9EA0` | 3.02 | 1 | ✓ 可一次替换 |
| `primaryForeground` | `#BBBBBB` | — | 3.81 | 7 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `hoverBackground` | `#666A6C` | — | 3.84 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `secondaryBackground` | `#12191F` | `#E4EAEE` | 4.50 | 1 | ✓ 可一次替换 |
| `#B4B8BF（内联）` | `#B4B8BF` | `#BDC1C6` | 4.50 | 1 | ✓ 可一次替换 |
| `primaryBackground` | `#3C3F41` | `#1A1B1C` | 4.52 | 1 | ✓ 可一次替换 |
| `#28B366（内联）` | `#28B366` | `#2FC170` | 4.54 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `underlineColor` | `#9876AA` | `#3C3F41`、`#666A6C`、`#BBBBBB` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `mainBackground` | `#4C5052` | `#4C5052`、`#666A6C`、`#9876AA`、`#B4B8BF`、`#BBBBBB` | 5 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `selectionBackground` | `#666A6C` | `#666A6C`、`#BBBBBB` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `primaryBackground` | `#3C3F41` | `#28B366`、`#BBBBBB` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `borderColor` = `#4C5052` | `mainBackground` = `#4C5052` | 1.00 | 3.0 | `#999EA1` → 3.01 ✓ | `#999EA1` → 3.01 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#666A6C` | `selectionBackground` = `#666A6C` | 1.00 | 4.5 | `#E9E9EA` → 4.50 ✓ | `#E9E9EA` → 4.50 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#666A6C` | `underlineColor` = `#9876AA` | 1.43 | 4.5 | `#1A1B1B` → 4.53 ✓ | `#EDE8F0` → 4.53 ✓ ⚠该底色还被别处用作前景 ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `lineColor` = `#666A6C` | `mainBackground` = `#4C5052` | 1.49 | 3.0 | `#9A9EA0` → 3.02 ✓ | `#1E1F20` → 3.02 ✓ | UI线条 lineColor |  |
| `primaryForeground` = `#BBBBBB` | `underlineColor` = `#9876AA` | 1.99 | 4.5 | `#1B1B1B` → 4.52 ✓ | `#5A4266` → 4.55 ✓ ⚠该底色还被别处用作前景 | UI 配对 SearchEverywhere.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `#8d6b81`（内联） = `#8D6B81` | 2.40 | 4.5 | `#FDFDFD` → 4.53 ✓ | `#594653` → 4.51 ✓ | UI 配对 Bookmark.MnemonicCurrent.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `#786299`（内联） = `#786299` | 2.73 | 4.5 | `#EEEEEE` → 4.52 ✓ | `#534568` → 4.52 ✓ | UI 配对 Bookmark.MnemonicAssigned.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `selectionBackground` = `#666A6C` | 2.85 | 4.5 | `#E9E9E9` → 4.50 ✓ | `#494C4D` → 4.51 ✓ | 正文 primaryForeground × selectionBackground |  |
| `primaryForeground` = `#BBBBBB` | `selectionInactiveBackground` = `#666A6C` | 2.85 | 4.5 | `#E9E9E9` → 4.50 ✓ | `#494C4D` → 4.51 ✓ | 正文 primaryForeground × selectionInactiveBackground |  |
| `primaryForeground` = `#BBBBBB` | `#666a6c`（内联） = `#666A6C` | 2.85 | 4.5 | `#E9E9E9` → 4.50 ✓ | `#494C4D` → 4.51 ✓ | UI 配对 Plugins.Button.updateForeground↔updateBackground |  |
| `primaryForeground` = `#BBBBBB` | `hoverBackground` = `#666A6C` | 2.85 | 4.5 | `#E9E9E9` → 4.50 ✓ | `#494C4D` → 4.51 ✓ ⚠该底色还被别处用作前景 | UI 配对 Plugins.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `mainBackground` = `#4C5052` | 4.25 | 4.5 | `#C1C1C1` → 4.53 ✓ | `#494C4E` → 4.51 ✓ | 正文 primaryForeground × mainBackground |  |
| `accentColor` = `#9876AA` | `mainBackground` = `#4C5052` | 2.14 | 3.0 | `#AF94BD` → 3.02 ✓ | `#37393B` → 3.04 ✓ | UI强调 accentColor |  |
| `underlineColor` = `#9876AA` | `mainBackground` = `#4C5052` | 2.14 | 3.0 | `#AF94BD` → 3.02 ✓ | `#37393B` → 3.04 ✓ | UI强调 underlineColor | 该 token 同时用作背景，替换需同步复核 |
| `iconBackground` = `#9876AA` | `mainBackground` = `#4C5052` | 2.14 | 3.0 | `#AF94BD` → 3.02 ✓ | `#37393B` → 3.04 ✓ | UI图标 iconBackground |  |
| `primaryBackground` = `#3C3F41` | `underlineColor` = `#9876AA` | 2.78 | 4.5 | `#1A1B1C` → 4.52 ✓ | `#B8A1C4` → 4.51 ✓ ⚠该底色还被别处用作前景 | UI 配对 Counter.foreground↔background | 该 token 同时用作背景，替换需同步复核 |
| `secondaryBackground` = `#12191F` | `iconHoverBackground` = `#666A6C` | 3.24 | 4.5 | `#E4EAEE` → 4.50 ✓ | `#7D8184` → 4.51 ✓ | UI 配对 Plugins.Button.installForeground↔installBackground | 该 token 同时用作背景，替换需同步复核 |
| `#28B366`（内联） = `#28B366` | `primaryBackground` = `#3C3F41` | 3.91 | 4.5 | `#2FC170` → 4.54 ✓ | `#333537` → 4.53 ✓ ⚠该底色还被别处用作前景 | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground |  |
| `#B4B8BF`（内联） = `#B4B8BF` | `mainBackground` = `#4C5052` | 4.09 | 4.5 | `#BDC1C6` → 4.50 ✓ | `#46494B` → 4.56 ✓ | UI 配对 TrialWidget.Active.foreground↔background |  |

### islands-dark/islands-dark-green

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `primaryForeground` | `#BBBBBB` | — | 2.72 | 8 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `hoverBackground` | `#305862` | — | 2.72 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `accentColor` | `#9876AA` | `#9C7BAD` | 3.02 | 1 | ✓ 可一次替换 |
| `lineColor` | `#426A74` | `#5A8F9D` | 3.02 | 1 | ✓ 可一次替换 |
| `borderColor` | `#1D4155` | `#408FBB` | 3.02 | 1 | ✓ 可一次替换 |
| `secondaryBackground` | `#19273D` | `#F8F8F8` | 4.51 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `mainBackground` | `#1D4155` | `#1D4155`、`#28B366`、`#426A74`、`#9876AA`、`#B4B8BF`、`#BBBBBB` | 6 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `underlineColor` | `#28B366` | `#002F38`、`#305862`、`#BBBBBB` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `selectionBackground` | `#305862` | `#305862`、`#BBBBBB` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `primaryBackground` | `#002F38` | `#43A0A8`、`#BBBBBB` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `borderColor` = `#1D4155` | `mainBackground` = `#1D4155` | 1.00 | 3.0 | `#408FBB` → 3.02 ✓ | `#408FBB` → 3.02 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#305862` | `selectionBackground` = `#305862` | 1.00 | 4.5 | `#A8CBD4` → 4.51 ✓ | `#A8CBD4` → 4.51 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#305862` | `underlineColor` = `#28B366` | 2.87 | 4.5 | `#22383D` → 4.54 ✓ | `#65DB9A` → 4.50 ✓ ⚠该底色还被别处用作前景 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核 |
| `primaryForeground` = `#BBBBBB` | `underlineColor` = `#28B366` | 1.42 | 4.5 | `#353535` → 4.52 ✓ | `#1B5535` → 4.56 ✓ ⚠该底色还被别处用作前景 | UI 配对 SearchEverywhere.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `#00A6A6`（内联） = `#00A6A6` | 1.56 | 4.5 | `#2E2E2E` → 4.53 ✓ | `#1A5353` → 4.55 ✓ | UI 配对 Plugins.Button.updateForeground↔updateBackground |  |
| `primaryForeground` = `#BBBBBB` | `#8d6b81`（内联） = `#8D6B81` | 2.40 | 4.5 | `#FDFDFD` → 4.53 ✓ | `#594653` → 4.51 ✓ | UI 配对 Bookmark.MnemonicCurrent.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `#786299`（内联） = `#786299` | 2.73 | 4.5 | `#EEEEEE` → 4.52 ✓ | `#534568` → 4.52 ✓ | UI 配对 Bookmark.MnemonicAssigned.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `selectionBackground` = `#305862` | 4.05 | 4.5 | `#C5C5C5` → 4.51 ✓ | `#305059` → 4.52 ✓ | 正文 primaryForeground × selectionBackground |  |
| `primaryForeground` = `#BBBBBB` | `selectionInactiveBackground` = `#305862` | 4.05 | 4.5 | `#C5C5C5` → 4.51 ✓ | `#305059` → 4.52 ✓ | 正文 primaryForeground × selectionInactiveBackground |  |
| `primaryForeground` = `#BBBBBB` | `hoverBackground` = `#305862` | 4.05 | 4.5 | `#C5C5C5` → 4.51 ✓ | `#305059` → 4.52 ✓ ⚠该底色还被别处用作前景 | UI 配对 Plugins.Tab.selectedForeground↔selectedBackground |  |
| `lineColor` = `#426A74` | `mainBackground` = `#1D4155` | 1.83 | 3.0 | `#5A8F9D` → 3.02 ✓ | `#0E1920` → 3.01 ✓ | UI线条 lineColor |  |
| `accentColor` = `#9876AA` | `mainBackground` = `#1D4155` | 2.84 | 3.0 | `#9C7BAD` → 3.02 ✓ | `#203C4C` → 3.04 ✓ | UI强调 accentColor |  |
| `secondaryBackground` = `#19273D` | `iconHoverBackground` = `#0B843E` | 3.13 | 4.5 | `#F8F8F8` → 4.51 ✓ | `#2AA15C` → 4.54 ✓ | UI 配对 Plugins.Button.installForeground↔installBackground | 该 token 同时用作背景，替换需同步复核 |

### islands-dark/islands-dark-violet

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `accentColor` | `#9876AA` | `#9F80B0` | 3.00 | 1 | ✓ 可一次替换 |
| `iconBackground` | `#9876AA` | `#9F80B0` | 3.00 | 1 | ✓ 可一次替换 |
| `lineColor` | `#9876AA` | `#9F80B0` | 3.00 | 1 | ✓ 可一次替换 |
| `borderColor` | `#433D5C` | `#8E87AF` | 3.03 | 1 | ✓ 可一次替换 |
| `underlineColor` | `#9876AA` | `#A385B3` | 3.18 | 2 | ✓ 可一次替换 |
| `primaryForeground` | `#BBBBBB` | — | 3.81 | 7 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `hoverBackground` | `#544E6B` | — | 3.81 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `primaryBackground` | `#2B2640` | `#1C1926` | 4.53 | 1 | ✓ 可一次替换 |
| `secondaryBackground` | `#0F0B1F` | `#C8C0E6` | 4.53 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `underlineColor` | `#9876AA` | `#2B2640`、`#544E6B`、`#BBBBBB` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `mainBackground` | `#433D5C` | `#433D5C`、`#9876AA`、`#B4B8BF`、`#BBBBBB` | 4 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `selectionBackground` | `#544E6B` | `#544E6B`、`#BBBBBB` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `primaryBackground` | `#2B2640` | `#9876AA`、`#BBBBBB` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `borderColor` = `#433D5C` | `mainBackground` = `#433D5C` | 1.00 | 3.0 | `#8E87AF` → 3.03 ✓ | `#8E87AF` → 3.03 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#544E6B` | `selectionBackground` = `#544E6B` | 1.00 | 4.5 | `#C6C3D2` → 4.53 ✓ | `#C6C3D2` → 4.53 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#544E6B` | `underlineColor` = `#9876AA` | 2.06 | 4.5 | `#1C1A22` → 4.51 ✓ | `#CFC0D7` → 4.54 ✓ ⚠该底色还被别处用作前景 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核 |
| `primaryForeground` = `#BBBBBB` | `underlineColor` = `#9876AA` | 1.99 | 4.5 | `#1B1B1B` → 4.52 ✓ | `#5A4266` → 4.55 ✓ ⚠该底色还被别处用作前景 | UI 配对 SearchEverywhere.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `#8d6b81`（内联） = `#8D6B81` | 2.40 | 4.5 | `#FDFDFD` → 4.53 ✓ | `#594653` → 4.51 ✓ | UI 配对 Bookmark.MnemonicCurrent.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `#786299`（内联） = `#786299` | 2.73 | 4.5 | `#EEEEEE` → 4.52 ✓ | `#534568` → 4.52 ✓ | UI 配对 Bookmark.MnemonicAssigned.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `selectionBackground` = `#544E6B` | 4.08 | 4.5 | `#C5C5C5` → 4.54 ✓ | `#4D4860` → 4.54 ✓ | 正文 primaryForeground × selectionBackground |  |
| `primaryForeground` = `#BBBBBB` | `selectionInactiveBackground` = `#544E6B` | 4.08 | 4.5 | `#C5C5C5` → 4.54 ✓ | `#4D4860` → 4.54 ✓ | 正文 primaryForeground × selectionInactiveBackground |  |
| `primaryForeground` = `#BBBBBB` | `#544e6b`（内联） = `#544E6B` | 4.08 | 4.5 | `#C5C5C5` → 4.54 ✓ | `#4D4860` → 4.54 ✓ | UI 配对 Plugins.Button.updateForeground↔updateBackground |  |
| `primaryForeground` = `#BBBBBB` | `hoverBackground` = `#544E6B` | 4.08 | 4.5 | `#C5C5C5` → 4.54 ✓ | `#4D4860` → 4.54 ✓ ⚠该底色还被别处用作前景 | UI 配对 Plugins.Tab.selectedForeground↔selectedBackground |  |
| `secondaryBackground` = `#0F0B1F` | `iconHoverBackground` = `#544E6B` | 2.46 | 4.5 | `#C8C0E6` → 4.53 ✓ | `#7E769B` → 4.55 ✓ | UI 配对 Plugins.Button.installForeground↔installBackground | 该 token 同时用作背景，替换需同步复核 |
| `accentColor` = `#9876AA` | `mainBackground` = `#433D5C` | 2.67 | 3.0 | `#9F80B0` → 3.00 ✓ | `#3B364D` → 3.02 ✓ | UI强调 accentColor |  |
| `underlineColor` = `#9876AA` | `mainBackground` = `#433D5C` | 2.67 | 3.0 | `#A385B3` → 3.18 ✓ | `#3B364D` → 3.02 ✓ | UI强调 underlineColor | 该 token 同时用作背景，替换需同步复核 |
| `underlineColor` = `#9876AA` | `primaryBackground` = `#2B2640` | 3.79 | 4.5 | `#A385B3` → 4.51 ✓ | `#1C1926` → 4.53 ✓ ⚠该底色还被别处用作前景 | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground | 该 token 同时用作背景，替换需同步复核 |
| `iconBackground` = `#9876AA` | `mainBackground` = `#433D5C` | 2.67 | 3.0 | `#9F80B0` → 3.00 ✓ | `#3B364D` → 3.02 ✓ | UI图标 iconBackground |  |
| `lineColor` = `#9876AA` | `mainBackground` = `#433D5C` | 2.67 | 3.0 | `#9F80B0` → 3.00 ✓ | `#3B364D` → 3.02 ✓ | UI线条 lineColor |  |
| `primaryBackground` = `#2B2640` | `underlineColor` = `#9876AA` | 3.79 | 4.5 | `#1C1926` → 4.53 ✓ | `#A385B3` → 4.51 ✓ ⚠该底色还被别处用作前景 | UI 配对 Counter.foreground↔background | 该 token 同时用作背景，替换需同步复核 |

### islands-dark/islands-dark

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `primaryForeground` | `#BBBBBB` | — | 2.72 | 7 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `hoverBackground` | `#515260` | — | 2.73 | 2 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `lineColor` | `#515260` | `#858698` | 3.01 | 1 | ✓ 可一次替换 |
| `borderColor` | `#3B3D49` | `#84879B` | 3.04 | 1 | ✓ 可一次替换 |
| `secondaryBackground` | `#19273D` | `#B8C8E0` | 4.54 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `mainBackground` | `#3B3D49` | `#28B366`、`#3B3D49`、`#515260`、`#B4B8BF`、`#BBBBBB` | 5 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `underlineColor` | `#28B366` | `#282A36`、`#515260`、`#BBBBBB` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `selectionBackground` | `#515260` | `#515260`、`#BBBBBB` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `primaryBackground` | `#282A36` | `#28B366`、`#BBBBBB` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `borderColor` = `#3B3D49` | `mainBackground` = `#3B3D49` | 1.00 | 3.0 | `#84879B` → 3.04 ✓ | `#84879B` → 3.04 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#515260` | `selectionBackground` = `#515260` | 1.00 | 4.5 | `#C5C6CE` → 4.53 ✓ | `#C5C6CE` → 4.53 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#515260` | `underlineColor` = `#28B366` | 2.84 | 4.5 | `#34343B` → 4.55 ✓ | `#68DC9C` → 4.51 ✓ ⚠该底色还被别处用作前景 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核 |
| `lineColor` = `#515260` | `mainBackground` = `#3B3D49` | 1.40 | 3.0 | `#858698` → 3.01 ✓ | `#9EA1B1` → 3.00 ✓ ⚠改动大 | UI线条 lineColor | 改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#BBBBBB` | `underlineColor` = `#28B366` | 1.42 | 4.5 | `#353535` → 4.52 ✓ | `#1B5535` → 4.56 ✓ ⚠该底色还被别处用作前景 | UI 配对 SearchEverywhere.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#BBBBBB` | `#8d6b81`（内联） = `#8D6B81` | 2.40 | 4.5 | `#FDFDFD` → 4.53 ✓ | `#594653` → 4.51 ✓ | UI 配对 Bookmark.MnemonicCurrent.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `#786299`（内联） = `#786299` | 2.73 | 4.5 | `#EEEEEE` → 4.52 ✓ | `#534568` → 4.52 ✓ | UI 配对 Bookmark.MnemonicAssigned.foreground↔background |  |
| `primaryForeground` = `#BBBBBB` | `selectionBackground` = `#515260` | 4.01 | 4.5 | `#C6C6C6` → 4.51 ✓ | `#4A4A56` → 4.55 ✓ | 正文 primaryForeground × selectionBackground |  |
| `primaryForeground` = `#BBBBBB` | `selectionInactiveBackground` = `#515260` | 4.01 | 4.5 | `#C6C6C6` → 4.51 ✓ | `#4A4A56` → 4.55 ✓ | 正文 primaryForeground × selectionInactiveBackground |  |
| `primaryForeground` = `#BBBBBB` | `#515260`（内联） = `#515260` | 4.01 | 4.5 | `#C6C6C6` → 4.51 ✓ | `#4A4A56` → 4.55 ✓ | UI 配对 Plugins.Button.updateForeground↔updateBackground |  |
| `primaryForeground` = `#BBBBBB` | `hoverBackground` = `#515260` | 4.01 | 4.5 | `#C6C6C6` → 4.51 ✓ | `#4A4A56` → 4.55 ✓ ⚠该底色还被别处用作前景 | UI 配对 Plugins.Tab.selectedForeground↔selectedBackground |  |
| `secondaryBackground` = `#19273D` | `iconHoverBackground` = `#515260` | 1.95 | 4.5 | `#B8C8E0` → 4.54 ✓ | `#8B8C9D` → 4.53 ✓ | UI 配对 Plugins.Button.installForeground↔installBackground | 该 token 同时用作背景，替换需同步复核 |

### islands-light/islands-bean-green

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `lineColor` | `#91CB9E` | `#4B9C5D` | 3.01 | 1 | ✓ 可一次替换 |
| `borderColor` | `#B8DDBD` | `#4C9C57` | 3.02 | 1 | ✓ 可一次替换 |
| `iconBackground` | `#26A69A` | `#299B90` | 3.03 | 1 | ✓ 可一次替换 |
| `error` | `#CE5E5E` | `#C53F3F` | 4.50 | 1 | ✓ 可一次替换 |
| `warning` | `#CC9D57` | `#8D6830` | 4.51 | 1 | ✓ 可一次替换 |
| `hoverBackground` | `#A9CBA3` | `#466A3F` | 4.51 | 2 | ✓ 可一次替换 |
| `info` | `#397FB7` | `#3673A4` | 4.52 | 1 | ✓ 可一次替换 |
| `success` | `#30A25E` | `#2B7D4C` | 4.52 | 1 | ✓ 可一次替换 |
| `#818594（内联）` | `#818594` | `#6A6E7D` | 4.52 | 1 | ✓ 可一次替换 |
| `warningFocused` | `#D09136` | `#916628` | 4.53 | 1 | ✓ 可一次替换 |
| `errorFocused` | `#CB4848` | `#C73B3B` | 4.55 | 1 | ✓ 可一次替换 |
| `primaryForeground` | `#1A536E` | `#1C475B` | 4.55 | 5 | ✓ 可一次替换 |
| `#0B843E（内联）` | `#0B843E` | `#216E41` | 4.87 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `mainBackground` | `#E3F7E7` | `#1A536E`、`#26A69A`、`#30A25E`、`#397FB7`、`#818594`、`#91CB9E`、`#B8DDBD`、`#CB4848`、`#CC9D57`、`#CE5E5E`、`#D09136` | 11 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `selectionBackground` | `#E2F9E1` | `#1A536E`、`#A9CBA3` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `editorBackground` | `#C7EDCC` | `#0B843E`、`#1A536E` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#A9CBA3` | `#50fa7b`（内联） = `#50FA7B` | 1.30 | 4.5 | `#466A3F` → 4.51 ✓ | `#1C5B2C` → 4.55 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#A9CBA3` | `selectionBackground` = `#E2F9E1` | 1.60 | 4.5 | `#466A3F` → 5.56 ✓ | `#1F5C1C` → 4.51 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `borderColor` = `#B8DDBD` | `mainBackground` = `#E3F7E7` | 1.33 | 3.0 | `#4C9C57` → 3.02 ✓ | `#2C883E` → 3.00 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `lineColor` = `#91CB9E` | `mainBackground` = `#E3F7E7` | 1.67 | 3.0 | `#4B9C5D` → 3.01 ✓ | `#287638` → 3.01 ✓ ⚠改动大 | UI线条 lineColor | 改背景需大幅调整明度，视觉影响大 |
| `warning` = `#CC9D57` | `mainBackground` = `#E3F7E7` | 2.20 | 4.5 | `#8D6830` → 4.51 ✓ | `#194422` → 4.51 ✓ ⚠改动大 | 语义文本 warning | 改背景需大幅调整明度，视觉影响大 |
| `warningFocused` = `#D09136` | `mainBackground` = `#E3F7E7` | 2.41 | 4.5 | `#916628` → 4.53 ✓ | `#173D1F` → 4.52 ✓ ⚠改动大 | 语义文本 warningFocused | 改背景需大幅调整明度，视觉影响大 |
| `iconBackground` = `#26A69A` | `mainBackground` = `#E3F7E7` | 2.67 | 3.0 | `#299B90` → 3.03 ✓ | `#1E5329` → 3.01 ✓ ⚠改动大 | UI图标 iconBackground | 改背景需大幅调整明度，视觉影响大 |
| `success` = `#30A25E` | `mainBackground` = `#E3F7E7` | 2.90 | 4.5 | `#2B7D4C` → 4.52 ✓ | `#122E17` → 4.52 ✓ ⚠改动大 | 语义文本 success | 改背景需大幅调整明度，视觉影响大 |
| `#818594`（内联） = `#818594` | `mainBackground` = `#E3F7E7` | 3.28 | 4.5 | `#6A6E7D` → 4.52 ✓ | `#0E2312` → 4.51 ✓ ⚠改动大 | UI 配对 TrialWidget.Active.foreground↔background | 改背景需大幅调整明度，视觉影响大 |
| `error` = `#CE5E5E` | `mainBackground` = `#E3F7E7` | 3.48 | 4.5 | `#C53F3F` → 4.50 ✓ | `#0B1C0F` → 4.54 ✓ ⚠改动大 | 语义文本 error | 改背景需大幅调整明度，视觉影响大 |
| `#0B843E`（内联） = `#0B843E` | `editorBackground` = `#C7EDCC` | 3.74 | 4.5 | `#216E41` → 4.87 ✓ | `#F2FAF3` → 4.50 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#1A536E` | `#2fc864`（内联） = `#2FC864` | 3.82 | 4.5 | `#1C475B` → 4.55 ✓ | `#52D680` → 4.50 ✓ | UI 配对 Counter.foreground↔background |  |
| `info` = `#397FB7` | `mainBackground` = `#E3F7E7` | 3.83 | 4.5 | `#3673A4` → 4.52 ✓ | `#071009` → 4.50 ✓ ⚠改动大 | 语义文本 info | 改背景需大幅调整明度，视觉影响大 |
| `errorFocused` = `#CB4848` | `mainBackground` = `#E3F7E7` | 4.11 | 4.5 | `#C73B3B` → 4.55 ✓ | `#FDFDFD` → 4.53 ✓ | 语义文本 errorFocused |  |

### islands-light/islands-buff

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `iconBackground` | `#DDB900` | `#918028` | 3.01 | 1 | ✓ 可一次替换 |
| `lineColor` | `#BE9B00` | `#927F28` | 3.03 | 1 | ✓ 可一次替换 |
| `borderColor` | `#E3E3DE` | `#81816E` | 3.03 | 1 | ✓ 可一次替换 |
| `primaryForeground` | `#333333` | — | 4.38 | 3 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `error` | `#CE5E5E` | `#B43838` | 4.50 | 1 | ✓ 可一次替换 |
| `accentColor` | `#BE9B00` | `#726421` | 4.50 | 2 | ✓ 可一次替换 |
| `warning` | `#FFAC4F` | `#895A26` | 4.51 | 1 | ✓ 可一次替换 |
| `warningFocused` | `#D09136` | `#835D25` | 4.51 | 1 | ✓ 可一次替换 |
| `#818594（内联）` | `#818594` | `#606471` | 4.51 | 1 | ✓ 可一次替换 |
| `hoverBackground` | `#DFE1E5` | `#4C515B` | 4.52 | 2 | ✓ 可一次替换 |
| `info` | `#397FB7` | `#326893` | 4.53 | 1 | ✓ 可一次替换 |
| `success` | `#30A25E` | `#287145` | 4.53 | 1 | ✓ 可一次替换 |
| `errorFocused` | `#CB4848` | `#B53535` | 4.55 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `mainBackground` | `#DFE1E5` | `#30A25E`、`#333333`、`#397FB7`、`#818594`、`#BE9B00`、`#CB4848`、`#CE5E5E`、`#D09136`、`#DDB900`、`#E3E3DE`、`#FFAC4F` | 11 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `selectionBackground` | `#DFE1E5` | `#333333`、`#BE9B00`、`#DFE1E5` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `menuBackground` | `#F7F7F2` | `#333333`、`#5A5D6B`、`#BE9B00` | 3 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `editorBackground` | `#F7F7F2` | `#333333`、`#5A5D6B`、`#BE9B00` | 3 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#DFE1E5` | `selectionBackground` = `#DFE1E5` | 1.00 | 4.5 | `#4C515B` → 6.09 ✓ | `#5D6471` → 4.55 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#DFE1E5` | `#c4c3bf`（内联） = `#C4C3BF` | 1.35 | 4.5 | `#4C515B` → 4.52 ✓ | `#66645E` → 4.52 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `borderColor` = `#E3E3DE` | `mainBackground` = `#DFE1E5` | 1.02 | 3.0 | `#81816E` → 3.03 ✓ | `#798292` → 3.01 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `warning` = `#FFAC4F` | `mainBackground` = `#DFE1E5` | 1.42 | 4.5 | `#895A26` → 4.51 ✓ | `#484D57` → 4.55 ✓ ⚠改动大 | 语义文本 warning | 改背景需大幅调整明度，视觉影响大 |
| `iconBackground` = `#DDB900` | `mainBackground` = `#DFE1E5` | 1.46 | 3.0 | `#918028` → 3.01 ✓ | `#5F6674` → 3.03 ✓ ⚠改动大 | UI图标 iconBackground | 改背景需大幅调整明度，视觉影响大 |
| `accentColor` = `#BE9B00` | `mainBackground` = `#DFE1E5` | 2.04 | 3.0 | `#726421` → 4.50 ✓ | `#4B505B` → 3.03 ✓ ⚠改动大 | UI强调 accentColor | 改背景需大幅调整明度，视觉影响大 |
| `accentColor` = `#BE9B00` | `selectionBackground` = `#DFE1E5` | 2.04 | 4.5 | `#726421` → 4.50 ✓ | `#33363D` → 4.54 ✓ ⚠改动大 | UI 配对 TrialWidget.Active.hoverForeground↔hoverBackground | 改背景需大幅调整明度，视觉影响大 |
| `accentColor` = `#BE9B00` | `editorBackground` = `#F7F7F2` | 2.48 | 4.5 | `#726421` → 5.49 ✓ | `#373725` → 4.54 ✓ ⚠改动大 | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground | 改背景需大幅调整明度，视觉影响大 |
| `accentColor` = `#BE9B00` | `menuBackground` = `#F7F7F2` | 2.48 | 4.5 | `#726421` → 5.49 ✓ | `#373725` → 4.54 ✓ ⚠改动大 | UI 配对 Plugins.Button.installForeground↔installBackground | 改背景需大幅调整明度，视觉影响大 |
| `lineColor` = `#BE9B00` | `mainBackground` = `#DFE1E5` | 2.04 | 3.0 | `#927F28` → 3.03 ✓ | `#4B505B` → 3.03 ✓ ⚠改动大 | UI线条 lineColor | 改背景需大幅调整明度，视觉影响大 |
| `warningFocused` = `#D09136` | `mainBackground` = `#DFE1E5` | 2.06 | 4.5 | `#835D25` → 4.51 ✓ | `#32353C` → 4.55 ✓ ⚠改动大 | 语义文本 warningFocused | 改背景需大幅调整明度，视觉影响大 |
| `success` = `#30A25E` | `mainBackground` = `#DFE1E5` | 2.48 | 4.5 | `#287145` → 4.53 ✓ | `#26282D` → 4.53 ✓ ⚠改动大 | 语义文本 success | 改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#333333` | `#0B843E`（内联） = `#0B843E` | 2.64 | 4.5 | `#F8F8F8` → 4.51 ✓ | `#2CB064` → 4.51 ✓ | UI 配对 Counter.foreground↔background |  |
| `#818594`（内联） = `#818594` | `mainBackground` = `#DFE1E5` | 2.81 | 4.5 | `#606471` → 4.51 ✓ | `#1D1E22` → 4.53 ✓ ⚠改动大 | UI 配对 TrialWidget.Active.foreground↔background | 改背景需大幅调整明度，视觉影响大 |
| `error` = `#CE5E5E` | `mainBackground` = `#DFE1E5` | 2.98 | 4.5 | `#B43838` → 4.50 ✓ | `#18191C` → 4.51 ✓ ⚠改动大 | 语义文本 error | 改背景需大幅调整明度，视觉影响大 |
| `info` = `#397FB7` | `mainBackground` = `#DFE1E5` | 3.28 | 4.5 | `#326893` → 4.53 ✓ | `#0D0E0F` → 4.50 ✓ ⚠改动大 | 语义文本 info | 改背景需大幅调整明度，视觉影响大 |
| `errorFocused` = `#CB4848` | `mainBackground` = `#DFE1E5` | 3.52 | 4.5 | `#B53535` → 4.55 ✓ | `#FDFDFD` → 4.53 ✓ | 语义文本 errorFocused |  |

### islands-light/islands-celadon

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `iconBackground` | `#26A69A` | `#26897F` | 3.00 | 1 | ✓ 可一次替换 |
| `lineColor` | `#70A6AE` | `#50838B` | 3.01 | 1 | ✓ 可一次替换 |
| `borderColor` | `#D3DCDF` | `#647F88` | 3.02 | 1 | ✓ 可一次替换 |
| `success` | `#30A25E` | `#266C43` | 4.51 | 1 | ✓ 可一次替换 |
| `#818594（内联）` | `#818594` | `#5C5F6B` | 4.51 | 1 | ✓ 可一次替换 |
| `error` | `#CE5E5E` | `#AA3636` | 4.53 | 1 | ✓ 可一次替换 |
| `errorFocused` | `#CB4848` | `#AC3434` | 4.53 | 1 | ✓ 可一次替换 |
| `warningFocused` | `#D09136` | `#7D5824` | 4.53 | 1 | ✓ 可一次替换 |
| `info` | `#397FB7` | `#30638C` | 4.53 | 1 | ✓ 可一次替换 |
| `warning` | `#CC9D57` | `#79592A` | 4.55 | 1 | ✓ 可一次替换 |
| `primaryForeground` | `#1A536E` | `#1C475B` | 4.55 | 4 | ✓ 可一次替换 |
| `hoverBackground` | `#C7DAE1` | `#3F626F` | 4.56 | 2 | ✓ 可一次替换 |
| `#0B843E（内联）` | `#0B843E` | `#216E41` | 5.20 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `mainBackground` | `#C4DEE4` | `#1A536E`、`#26A69A`、`#30A25E`、`#397FB7`、`#70A6AE`、`#818594`、`#CB4848`、`#CC9D57`、`#CE5E5E`、`#D09136`、`#D3DCDF` | 11 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `selectionBackground` | `#C7DAE1` | `#1A536E`、`#C7DAE1` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `menuBackground` | `#E4ECEF` | `#0B843E`、`#1A536E` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#C7DAE1` | `selectionBackground` = `#C7DAE1` | 1.00 | 4.5 | `#3F626F` → 4.56 ✓ | `#3F626F` → 4.56 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#C7DAE1` | `#50fa7b`（内联） = `#50FA7B` | 1.05 | 4.5 | `#3F626F` → 4.80 ✓ | `#206B33` → 4.53 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `borderColor` = `#D3DCDF` | `mainBackground` = `#C4DEE4` | 1.01 | 3.0 | `#647F88` → 3.02 ✓ | `#458492` → 3.03 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `warning` = `#CC9D57` | `mainBackground` = `#C4DEE4` | 1.75 | 4.5 | `#79592A` → 4.55 ✓ | `#243F45` → 4.56 ✓ ⚠改动大 | 语义文本 warning | 改背景需大幅调整明度，视觉影响大 |
| `warningFocused` = `#D09136` | `mainBackground` = `#C4DEE4` | 1.92 | 4.5 | `#7D5824` → 4.53 ✓ | `#21393E` → 4.53 ✓ ⚠改动大 | 语义文本 warningFocused | 改背景需大幅调整明度，视觉影响大 |
| `lineColor` = `#70A6AE` | `mainBackground` = `#C4DEE4` | 1.92 | 3.0 | `#50838B` → 3.01 ✓ | `#2F545D` → 3.04 ✓ ⚠改动大 | UI线条 lineColor | 改背景需大幅调整明度，视觉影响大 |
| `iconBackground` = `#26A69A` | `mainBackground` = `#C4DEE4` | 2.13 | 3.0 | `#26897F` → 3.00 ✓ | `#2C4E56` → 3.00 ✓ ⚠改动大 | UI图标 iconBackground | 改背景需大幅调整明度，视觉影响大 |
| `success` = `#30A25E` | `mainBackground` = `#C4DEE4` | 2.31 | 4.5 | `#266C43` → 4.51 ✓ | `#1A2B2F` → 4.51 ✓ ⚠改动大 | 语义文本 success | 改背景需大幅调整明度，视觉影响大 |
| `#818594`（内联） = `#818594` | `mainBackground` = `#C4DEE4` | 2.61 | 4.5 | `#5C5F6B` → 4.51 ✓ | `#132023` → 4.54 ✓ ⚠改动大 | UI 配对 TrialWidget.Active.foreground↔background | 改背景需大幅调整明度，视觉影响大 |
| `error` = `#CE5E5E` | `mainBackground` = `#C4DEE4` | 2.77 | 4.5 | `#AA3636` → 4.53 ✓ | `#101A1D` → 4.54 ✓ ⚠改动大 | 语义文本 error | 改背景需大幅调整明度，视觉影响大 |
| `info` = `#397FB7` | `mainBackground` = `#C4DEE4` | 3.05 | 4.5 | `#30638C` → 4.53 ✓ | `#090F10` → 4.50 ✓ ⚠改动大 | 语义文本 info | 改背景需大幅调整明度，视觉影响大 |
| `errorFocused` = `#CB4848` | `mainBackground` = `#C4DEE4` | 3.27 | 4.5 | `#AC3434` → 4.53 ✓ | `#FDFDFD` → 4.53 ✓ | 语义文本 errorFocused |  |
| `primaryForeground` = `#1A536E` | `#2fc864`（内联） = `#2FC864` | 3.82 | 4.5 | `#1C475B` → 4.55 ✓ | `#52D680` → 4.50 ✓ | UI 配对 Counter.foreground↔background |  |
| `#0B843E`（内联） = `#0B843E` | `menuBackground` = `#E4ECEF` | 4.00 | 4.5 | `#216E41` → 5.20 ✓ | `#F8F8F8` → 4.51 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground |  |

### islands-light/islands-green-contrast

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `iconBackground` | `#70A6AE` | `#59949D` | 3.00 | 1 | ✓ 可一次替换 |
| `lineColor` | `#A1D6D7` | `#449799` | 3.01 | 1 | ✓ 可一次替换 |
| `borderColor` | `#C1DCDC` | `#559595` | 3.02 | 1 | ✓ 可一次替换 |
| `warning` | `#CC9D57` | `#8C672F` | 4.50 | 1 | ✓ 可一次替换 |
| `errorFocused` | `#CB4848` | `#C73A3A` | 4.51 | 1 | ✓ 可一次替换 |
| `info` | `#397FB7` | `#3672A3` | 4.51 | 1 | ✓ 可一次替换 |
| `success` | `#30A25E` | `#2B7C4C` | 4.51 | 1 | ✓ 可一次替换 |
| `#818594（内联）` | `#818594` | `#6A6D7C` | 4.51 | 1 | ✓ 可一次替换 |
| `error` | `#CE5E5E` | `#C43D3D` | 4.52 | 1 | ✓ 可一次替换 |
| `hoverBackground` | `#A3CED0` | `#33595B` | 4.52 | 2 | ✓ 可一次替换 |
| `warningFocused` | `#D09136` | `#906527` | 4.53 | 1 | ✓ 可一次替换 |
| `#0B843E（内联）` | `#0B843E` | `#1D5E38` | 4.54 | 1 | ✓ 可一次替换 |
| `primaryForeground` | `#1A536E` | `#1C475B` | 4.55 | 5 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `mainBackground` | `#E4F3F6` | `#1A536E`、`#30A25E`、`#397FB7`、`#70A6AE`、`#818594`、`#A1D6D7`、`#C1DCDC`、`#CB4848`、`#CC9D57`、`#CE5E5E`、`#D09136` | 11 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `selectionBackground` | `#A3CED0` | `#1A536E`、`#A3CED0` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `tabHoverBackground` | `#A3CED0` | `#0B843E`、`#1A536E` | 2 | ✓ 可一次替换 |  |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#A3CED0` | `selectionBackground` = `#A3CED0` | 1.00 | 4.5 | `#33595B` → 4.52 ✓ | `#33595B` → 4.52 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#A3CED0` | `lineColor` = `#A1D6D7` | 1.07 | 4.5 | `#33595B` → 4.82 ✓ | `#2C5A5B` → 4.52 ✓ ⚠该底色还被别处用作前景 ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `borderColor` = `#C1DCDC` | `mainBackground` = `#E4F3F6` | 1.27 | 3.0 | `#559595` → 3.02 ✓ | `#358393` → 3.02 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `lineColor` = `#A1D6D7` | `mainBackground` = `#E4F3F6` | 1.41 | 3.0 | `#449799` → 3.01 ✓ | `#327B89` → 3.03 ✓ ⚠改动大 | UI线条 lineColor | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `warning` = `#CC9D57` | `mainBackground` = `#E4F3F6` | 2.16 | 4.5 | `#8C672F` → 4.50 ✓ | `#1D4047` → 4.54 ✓ ⚠改动大 | 语义文本 warning | 改背景需大幅调整明度，视觉影响大 |
| `warningFocused` = `#D09136` | `mainBackground` = `#E4F3F6` | 2.37 | 4.5 | `#906527` → 4.53 ✓ | `#1B3A41` → 4.51 ✓ ⚠改动大 | 语义文本 warningFocused | 改背景需大幅调整明度，视觉影响大 |
| `iconBackground` = `#70A6AE` | `mainBackground` = `#E4F3F6` | 2.38 | 3.0 | `#59949D` → 3.00 ✓ | `#265660` → 3.00 ✓ ⚠改动大 | UI图标 iconBackground | 改背景需大幅调整明度，视觉影响大 |
| `#0B843E`（内联） = `#0B843E` | `tabHoverBackground` = `#A3CED0` | 2.80 | 4.5 | `#1D5E38` → 4.54 ✓ | `#F5F9F9` → 4.52 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground |  |
| `success` = `#30A25E` | `mainBackground` = `#E4F3F6` | 2.86 | 4.5 | `#2B7C4C` → 4.51 ✓ | `#142B2F` → 4.56 ✓ ⚠改动大 | 语义文本 success | 改背景需大幅调整明度，视觉影响大 |
| `#818594`（内联） = `#818594` | `mainBackground` = `#E4F3F6` | 3.23 | 4.5 | `#6A6D7C` → 4.51 ✓ | `#102124` → 4.52 ✓ ⚠改动大 | UI 配对 TrialWidget.Active.foreground↔background | 改背景需大幅调整明度，视觉影响大 |
| `error` = `#CE5E5E` | `mainBackground` = `#E4F3F6` | 3.42 | 4.5 | `#C43D3D` → 4.52 ✓ | `#0D1B1E` → 4.52 ✓ ⚠改动大 | 语义文本 error | 改背景需大幅调整明度，视觉影响大 |
| `info` = `#397FB7` | `mainBackground` = `#E4F3F6` | 3.77 | 4.5 | `#3672A3` → 4.51 ✓ | `#080F11` → 4.50 ✓ ⚠改动大 | 语义文本 info | 改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#1A536E` | `#2fc864`（内联） = `#2FC864` | 3.82 | 4.5 | `#1C475B` → 4.55 ✓ | `#52D680` → 4.50 ✓ | UI 配对 Counter.foreground↔background |  |
| `errorFocused` = `#CB4848` | `mainBackground` = `#E4F3F6` | 4.05 | 4.5 | `#C73A3A` → 4.51 ✓ | `#FDFDFD` → 4.53 ✓ | 语义文本 errorFocused |  |

### islands-light/islands-green

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `borderColor` | `#CCDEDD` | `#568380` | 3.00 | 1 | ✓ 可一次替换 |
| `iconBackground` | `#70A6AE` | `#50828A` | 3.03 | 1 | ✓ 可一次替换 |
| `lineColor` | `#A1D6D7` | `#3D8586` | 3.04 | 1 | ✓ 可一次替换 |
| `errorFocused` | `#CB4848` | `#AC3434` | 4.51 | 1 | ✓ 可一次替换 |
| `warningFocused` | `#D09136` | `#7D5824` | 4.51 | 1 | ✓ 可一次替换 |
| `error` | `#CE5E5E` | `#AA3636` | 4.52 | 1 | ✓ 可一次替换 |
| `info` | `#397FB7` | `#30638C` | 4.52 | 1 | ✓ 可一次替换 |
| `#0B843E（内联）` | `#0B843E` | `#1E633B` | 4.52 | 1 | ✓ 可一次替换 |
| `hoverBackground` | `#A1D6D7` | `#32696A` | 4.53 | 2 | ✓ 可一次替换 |
| `warning` | `#CC9D57` | `#79592A` | 4.54 | 1 | ✓ 可一次替换 |
| `success` | `#30A25E` | `#266B42` | 4.55 | 1 | ✓ 可一次替换 |
| `primaryForeground` | `#1A536E` | `#1C475B` | 4.55 | 5 | ✓ 可一次替换 |
| `#818594（内联）` | `#818594` | `#5B5E6A` | 4.57 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `mainBackground` | `#C5DEDD` | `#1A536E`、`#30A25E`、`#397FB7`、`#70A6AE`、`#818594`、`#A1D6D7`、`#CB4848`、`#CC9D57`、`#CCDEDD`、`#CE5E5E`、`#D09136` | 11 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `selectionBackground` | `#C6E1E0` | `#1A536E`、`#A1D6D7` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `tabHoverBackground` | `#A1D6D7` | `#0B843E`、`#1A536E` | 2 | ✓ 可一次替换 |  |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `borderColor` = `#CCDEDD` | `mainBackground` = `#C5DEDD` | 1.01 | 3.0 | `#568380` → 3.00 ✓ | `#4E8582` → 3.01 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `lineColor` = `#A1D6D7` | `mainBackground` = `#C5DEDD` | 1.13 | 3.0 | `#3D8586` → 3.04 ✓ | `#487A78` → 3.03 ✓ ⚠改动大 | UI线条 lineColor | 改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#A1D6D7` | `selectionBackground` = `#C6E1E0` | 1.16 | 4.5 | `#32696A` → 4.53 ✓ | `#355D5C` → 4.57 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#A1D6D7` | `#50fa7b`（内联） = `#50FA7B` | 1.17 | 4.5 | `#32696A` → 4.55 ✓ | `#1E632F` → 4.55 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `warning` = `#CC9D57` | `mainBackground` = `#C5DEDD` | 1.74 | 4.5 | `#79592A` → 4.54 ✓ | `#283F3F` → 4.55 ✓ ⚠改动大 | 语义文本 warning | 改背景需大幅调整明度，视觉影响大 |
| `warningFocused` = `#D09136` | `mainBackground` = `#C5DEDD` | 1.91 | 4.5 | `#7D5824` → 4.51 ✓ | `#243938` → 4.53 ✓ ⚠改动大 | 语义文本 warningFocused | 改背景需大幅调整明度，视觉影响大 |
| `iconBackground` = `#70A6AE` | `mainBackground` = `#C5DEDD` | 1.92 | 3.0 | `#50828A` → 3.03 ✓ | `#345554` → 3.01 ✓ ⚠改动大 | UI图标 iconBackground | 改背景需大幅调整明度，视觉影响大 |
| `success` = `#30A25E` | `mainBackground` = `#C5DEDD` | 2.30 | 4.5 | `#266B42` → 4.55 ✓ | `#1B2B2A` → 4.53 ✓ ⚠改动大 | 语义文本 success | 改背景需大幅调整明度，视觉影响大 |
| `#818594`（内联） = `#818594` | `mainBackground` = `#C5DEDD` | 2.60 | 4.5 | `#5B5E6A` → 4.57 ✓ | `#15201F` → 4.54 ✓ ⚠改动大 | UI 配对 TrialWidget.Active.foreground↔background | 改背景需大幅调整明度，视觉影响大 |
| `error` = `#CE5E5E` | `mainBackground` = `#C5DEDD` | 2.76 | 4.5 | `#AA3636` → 4.52 ✓ | `#111A1A` → 4.54 ✓ ⚠改动大 | 语义文本 error | 改背景需大幅调整明度，视觉影响大 |
| `#0B843E`（内联） = `#0B843E` | `tabHoverBackground` = `#A1D6D7` | 2.99 | 4.5 | `#1E633B` → 4.52 ✓ | `#F4F9F9` → 4.51 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground |  |
| `info` = `#397FB7` | `mainBackground` = `#C5DEDD` | 3.04 | 4.5 | `#30638C` → 4.52 ✓ | `#090E0E` → 4.53 ✓ ⚠改动大 | 语义文本 info | 改背景需大幅调整明度，视觉影响大 |
| `errorFocused` = `#CB4848` | `mainBackground` = `#C5DEDD` | 3.26 | 4.5 | `#AC3434` → 4.51 ✓ | `#FDFDFD` → 4.53 ✓ | 语义文本 errorFocused |  |
| `primaryForeground` = `#1A536E` | `#2fc864`（内联） = `#2FC864` | 3.82 | 4.5 | `#1C475B` → 4.55 ✓ | `#52D680` → 4.50 ✓ | UI 配对 Counter.foreground↔background |  |

### islands-light/islands-grey

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `lineColor` | `#A8ADBD` | `#788099` | 3.00 | 1 | ✓ 可一次替换 |
| `iconBackground` | `#739FA8` | `#5B8790` | 3.02 | 1 | ✓ 可一次替换 |
| `borderColor` | `#E0E4E4` | `#738383` | 3.02 | 1 | ✓ 可一次替换 |
| `error` | `#CE5E5E` | `#B43838` | 4.50 | 1 | ✓ 可一次替换 |
| `warning` | `#CC9D57` | `#805E2C` | 4.51 | 1 | ✓ 可一次替换 |
| `warningFocused` | `#D09136` | `#835D25` | 4.51 | 1 | ✓ 可一次替换 |
| `#818594（内联）` | `#818594` | `#606471` | 4.51 | 1 | ✓ 可一次替换 |
| `info` | `#397FB7` | `#326893` | 4.53 | 1 | ✓ 可一次替换 |
| `success` | `#30A25E` | `#287145` | 4.53 | 1 | ✓ 可一次替换 |
| `hoverBackground` | `#C9CCD6` | `#3D414E` | 4.54 | 2 | ✓ 可一次替换 |
| `errorFocused` | `#CB4848` | `#B53535` | 4.55 | 1 | ✓ 可一次替换 |
| `primaryForeground` | `#1A536E` | `#1C475B` | 4.55 | 4 | ✓ 可一次替换 |
| `#0B843E（内联）` | `#0B843E` | `#216E41` | 5.62 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `mainBackground` | `#DFE1E5` | `#1A536E`、`#30A25E`、`#397FB7`、`#739FA8`、`#818594`、`#A8ADBD`、`#CB4848`、`#CC9D57`、`#CE5E5E`、`#D09136`、`#E0E4E4` | 11 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `selectionBackground` | `#DFE1E5` | `#1A536E`、`#C9CCD6` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `editorBackground` | `#F0F4F4` | `#0B843E`、`#1A536E` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `borderColor` = `#E0E4E4` | `mainBackground` = `#DFE1E5` | 1.02 | 3.0 | `#738383` → 3.02 ✓ | `#7A8293` → 3.01 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#C9CCD6` | `selectionBackground` = `#DFE1E5` | 1.23 | 4.5 | `#3D414E` → 7.77 ✓ | `#515762` → 4.53 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#C9CCD6` | `lineColor` = `#A8ADBD` | 1.40 | 4.5 | `#3D414E` → 4.54 ✓ | `#515668` → 4.54 ✓ ⚠该底色还被别处用作前景 ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `lineColor` = `#A8ADBD` | `mainBackground` = `#DFE1E5` | 1.71 | 3.0 | `#788099` → 3.00 ✓ | `#555B67` → 3.05 ✓ ⚠改动大 | UI线条 lineColor | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `warning` = `#CC9D57` | `mainBackground` = `#DFE1E5` | 1.88 | 4.5 | `#805E2C` → 4.51 ✓ | `#383B43` → 4.55 ✓ ⚠改动大 | 语义文本 warning | 改背景需大幅调整明度，视觉影响大 |
| `warningFocused` = `#D09136` | `mainBackground` = `#DFE1E5` | 2.06 | 4.5 | `#835D25` → 4.51 ✓ | `#32353C` → 4.55 ✓ ⚠改动大 | 语义文本 warningFocused | 改背景需大幅调整明度，视觉影响大 |
| `iconBackground` = `#739FA8` | `mainBackground` = `#DFE1E5` | 2.21 | 3.0 | `#5B8790` → 3.02 ✓ | `#474B55` → 3.02 ✓ ⚠改动大 | UI图标 iconBackground | 改背景需大幅调整明度，视觉影响大 |
| `success` = `#30A25E` | `mainBackground` = `#DFE1E5` | 2.48 | 4.5 | `#287145` → 4.53 ✓ | `#26282D` → 4.53 ✓ ⚠改动大 | 语义文本 success | 改背景需大幅调整明度，视觉影响大 |
| `#818594`（内联） = `#818594` | `mainBackground` = `#DFE1E5` | 2.81 | 4.5 | `#606471` → 4.51 ✓ | `#1D1E22` → 4.53 ✓ ⚠改动大 | UI 配对 TrialWidget.Active.foreground↔background | 改背景需大幅调整明度，视觉影响大 |
| `error` = `#CE5E5E` | `mainBackground` = `#DFE1E5` | 2.98 | 4.5 | `#B43838` → 4.50 ✓ | `#18191C` → 4.51 ✓ ⚠改动大 | 语义文本 error | 改背景需大幅调整明度，视觉影响大 |
| `info` = `#397FB7` | `mainBackground` = `#DFE1E5` | 3.28 | 4.5 | `#326893` → 4.53 ✓ | `#0D0E0F` → 4.50 ✓ ⚠改动大 | 语义文本 info | 改背景需大幅调整明度，视觉影响大 |
| `errorFocused` = `#CB4848` | `mainBackground` = `#DFE1E5` | 3.52 | 4.5 | `#B53535` → 4.55 ✓ | `#FDFDFD` → 4.53 ✓ | 语义文本 errorFocused |  |
| `primaryForeground` = `#1A536E` | `#2fc864`（内联） = `#2FC864` | 3.82 | 4.5 | `#1C475B` → 4.55 ✓ | `#52D680` → 4.50 ✓ | UI 配对 Counter.foreground↔background |  |
| `#0B843E`（内联） = `#0B843E` | `editorBackground` = `#F0F4F4` | 4.32 | 4.5 | `#216E41` → 5.62 ✓ | `#F8F8F8` → 4.51 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground |  |

### islands-light/islands-pink

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `iconBackground` | `#FF79C6` | `#D95CA4` | 3.01 | 1 | ✓ 可一次替换 |
| `lineColor` | `#F7B9C4` | `#DA6277` | 3.02 | 1 | ✓ 可一次替换 |
| `borderColor` | `#EDE5E6` | `#A67F84` | 3.02 | 1 | ✓ 可一次替换 |
| `warning` | `#FFAC4F` | `#956128` | 4.50 | 1 | ✓ 可一次替换 |
| `hoverBackground` | `#FFD6E0` | `#A32A48` | 4.51 | 2 | ✓ 可一次替换 |
| `#818594（内联）` | `#818594` | `#686C7A` | 4.51 | 1 | ✓ 可一次替换 |
| `warningFocused` | `#D09136` | `#8E6427` | 4.52 | 1 | ✓ 可一次替换 |
| `errorFocused` | `#CB4848` | `#C53838` | 4.53 | 1 | ✓ 可一次替换 |
| `#DB58B0（内联）` | `#DB58B0` | `#C83096` | 4.53 | 1 | ✓ 可一次替换 |
| `error` | `#CE5E5E` | `#C23B3B` | 4.54 | 1 | ✓ 可一次替换 |
| `info` | `#397FB7` | `#3570A0` | 4.55 | 1 | ✓ 可一次替换 |
| `success` | `#30A25E` | `#2A7A4A` | 4.55 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `mainBackground` | `#FFE9EA` | `#30A25E`、`#397FB7`、`#3D0013`、`#818594`、`#964A6E`、`#CB4848`、`#CE5E5E`、`#D09136`、`#EDE5E6`、`#F7B9C4`、`#FF79C6`、`#FFAC4F` | 12 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `selectionBackground` | `#FFD6E0` | `#3D0013`、`#964A6E`、`#FFD6E0` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `editorBackground` | `#FDF5F6` | `#3D0013`、`#DB58B0` | 2 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `menuBackground` | `#FDF5F6` | `#3D0013`、`#964A6E` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#FFD6E0` | `selectionBackground` = `#FFD6E0` | 1.00 | 4.5 | `#A32A48` → 5.35 ✓ | `#B82E4F` → 4.50 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#FFD6E0` | `#ffbdd2`（内联） = `#FFBDD2` | 1.18 | 4.5 | `#A32A48` → 4.51 ✓ | `#B72D59` → 4.51 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `borderColor` = `#EDE5E6` | `mainBackground` = `#FFE9EA` | 1.07 | 3.0 | `#A67F84` → 3.02 ✓ | `#D85B61` → 3.03 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `lineColor` = `#F7B9C4` | `mainBackground` = `#FFE9EA` | 1.43 | 3.0 | `#DA6277` → 3.02 ✓ | `#CF343B` → 3.02 ✓ ⚠改动大 | UI线条 lineColor | 改背景需大幅调整明度，视觉影响大 |
| `warning` = `#FFAC4F` | `mainBackground` = `#FFE9EA` | 1.61 | 4.5 | `#956128` → 4.50 ✓ | `#8F272C` → 4.52 ✓ ⚠改动大 | 语义文本 warning | 改背景需大幅调整明度，视觉影响大 |
| `iconBackground` = `#FF79C6` | `mainBackground` = `#FFE9EA` | 2.06 | 3.0 | `#D95CA4` → 3.01 ✓ | `#A32A30` → 3.00 ✓ ⚠改动大 | UI图标 iconBackground | 改背景需大幅调整明度，视觉影响大 |
| `warningFocused` = `#D09136` | `mainBackground` = `#FFE9EA` | 2.32 | 4.5 | `#8E6427` → 4.52 ✓ | `#621E21` → 4.51 ✓ ⚠改动大 | 语义文本 warningFocused | 改背景需大幅调整明度，视觉影响大 |
| `success` = `#30A25E` | `mainBackground` = `#FFE9EA` | 2.80 | 4.5 | `#2A7A4A` → 4.55 ✓ | `#49181A` → 4.52 ✓ ⚠改动大 | 语义文本 success | 改背景需大幅调整明度，视觉影响大 |
| `#818594`（内联） = `#818594` | `mainBackground` = `#FFE9EA` | 3.17 | 4.5 | `#686C7A` → 4.51 ✓ | `#371314` → 4.52 ✓ ⚠改动大 | UI 配对 TrialWidget.Active.foreground↔background | 改背景需大幅调整明度，视觉影响大 |
| `#DB58B0`（内联） = `#DB58B0` | `editorBackground` = `#FDF5F6` | 3.24 | 4.5 | `#C83096` → 4.53 ✓ | `#3F151A` → 4.53 ✓ ⚠改动大 | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground | 改背景需大幅调整明度，视觉影响大 |
| `error` = `#CE5E5E` | `mainBackground` = `#FFE9EA` | 3.36 | 4.5 | `#C23B3B` → 4.54 ✓ | `#2D0F11` → 4.53 ✓ ⚠改动大 | 语义文本 error | 改背景需大幅调整明度，视觉影响大 |
| `info` = `#397FB7` | `mainBackground` = `#FFE9EA` | 3.70 | 4.5 | `#3570A0` → 4.55 ✓ | `#1A090A` → 4.50 ✓ ⚠改动大 | 语义文本 info | 改背景需大幅调整明度，视觉影响大 |
| `errorFocused` = `#CB4848` | `mainBackground` = `#FFE9EA` | 3.97 | 4.5 | `#C53838` → 4.53 ✓ | `#FDFDFD` → 4.53 ✓ | 语义文本 errorFocused |  |

### islands-light/islands-yellow

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `borderColor` | `#EFEBD8` | `#9C8C42` | 3.00 | 1 | ✓ 可一次替换 |
| `iconBackground` | `#DDB900` | `#9E8B2A` | 3.03 | 1 | ✓ 可一次替换 |
| `lineColor` | `#BE9B00` | `#968228` | 3.40 | 1 | ✓ 可一次替换 |
| `primaryForeground` | `#333333` | — | 4.38 | 4 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `errorFocused` | `#CB4848` | `#C83C3C` | 4.50 | 1 | ✓ 可一次替换 |
| `error` | `#CE5E5E` | `#C53F3F` | 4.51 | 1 | ✓ 可一次替换 |
| `warning` | `#FFAC4F` | `#986329` | 4.52 | 1 | ✓ 可一次替换 |
| `info` | `#397FB7` | `#3673A4` | 4.52 | 1 | ✓ 可一次替换 |
| `hoverBackground` | `#CCC8B6` | `#595440` | 4.52 | 1 | ✓ 可一次替换 |
| `success` | `#30A25E` | `#2B7D4C` | 4.53 | 1 | ✓ 可一次替换 |
| `#818594（内联）` | `#818594` | `#6A6E7D` | 4.53 | 1 | ✓ 可一次替换 |
| `warningFocused` | `#D09136` | `#916628` | 4.54 | 1 | ✓ 可一次替换 |
| `accentColor` | `#BE9B00` | `#60531D` | 4.54 | 3 | ✓ 可一次替换 |
| `#A46704（内联）` | `#A46704` | `#825F25` | 5.60 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `mainBackground` | `#FFF1D1` | `#30A25E`、`#333333`、`#397FB7`、`#818594`、`#BE9B00`、`#CB4848`、`#CE5E5E`、`#D09136`、`#DDB900`、`#EFEBD8`、`#FFAC4F` | 11 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `selectionBackground` | `#CCC8B6` | `#333333`、`#BE9B00`、`#CCC8B6` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `menuBackground` | `#FFFBE8` | `#333333`、`#BE9B00` | 2 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `editorBackground` | `#FFFBE8` | `#333333`、`#A46704` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#CCC8B6` | `#ccc8b6`（内联） = `#CCC8B6` | 1.00 | 4.5 | `#595440` → 4.52 ✓ | `#595440` → 4.52 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#CCC8B6` | `selectionBackground` = `#CCC8B6` | 1.00 | 4.5 | `#595440` → 4.52 ✓ | `#595440` → 4.52 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `borderColor` = `#EFEBD8` | `mainBackground` = `#FFF1D1` | 1.07 | 3.0 | `#9C8C42` → 3.00 ✓ | `#A7812B` → 3.02 ✓ ⚠改动大 | UI边框 borderColor | 改背景需大幅调整明度，视觉影响大 |
| `accentColor` = `#BE9B00` | `selectionBackground` = `#CCC8B6` | 1.59 | 4.5 | `#60531D` → 4.54 ✓ | `#39362A` → 4.54 ✓ ⚠改动大 | UI 配对 TrialWidget.Active.hoverForeground↔hoverBackground | 改背景需大幅调整明度，视觉影响大 |
| `accentColor` = `#BE9B00` | `mainBackground` = `#FFF1D1` | 2.38 | 3.0 | `#60531D` → 6.81 ✓ | `#624D1E` → 3.03 ✓ ⚠改动大 | UI强调 accentColor | 改背景需大幅调整明度，视觉影响大 |
| `accentColor` = `#BE9B00` | `menuBackground` = `#FFFBE8` | 2.57 | 4.5 | `#60531D` → 7.34 ✓ | `#3D3614` → 4.54 ✓ ⚠改动大 | UI 配对 Plugins.Button.installForeground↔installBackground | 改背景需大幅调整明度，视觉影响大 |
| `warning` = `#FFAC4F` | `mainBackground` = `#FFF1D1` | 1.66 | 4.5 | `#986329` → 4.52 ✓ | `#5E4A1D` → 4.56 ✓ ⚠改动大 | 语义文本 warning | 改背景需大幅调整明度，视觉影响大 |
| `iconBackground` = `#DDB900` | `mainBackground` = `#FFF1D1` | 1.70 | 3.0 | `#9E8B2A` → 3.03 ✓ | `#7D6224` → 3.02 ✓ ⚠改动大 | UI图标 iconBackground | 改背景需大幅调整明度，视觉影响大 |
| `lineColor` = `#BE9B00` | `mainBackground` = `#FFF1D1` | 2.38 | 3.0 | `#968228` → 3.40 ✓ | `#624D1E` → 3.03 ✓ ⚠改动大 | UI线条 lineColor | 改背景需大幅调整明度，视觉影响大 |
| `warningFocused` = `#D09136` | `mainBackground` = `#FFF1D1` | 2.41 | 4.5 | `#916628` → 4.54 ✓ | `#413415` → 4.51 ✓ ⚠改动大 | 语义文本 warningFocused | 改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#333333` | `#0B843E`（内联） = `#0B843E` | 2.64 | 4.5 | `#F8F8F8` → 4.51 ✓ | `#2CB064` → 4.51 ✓ | UI 配对 Counter.foreground↔background |  |
| `success` = `#30A25E` | `mainBackground` = `#FFF1D1` | 2.91 | 4.5 | `#2B7D4C` → 4.53 ✓ | `#302711` → 4.53 ✓ ⚠改动大 | 语义文本 success | 改背景需大幅调整明度，视觉影响大 |
| `#818594`（内联） = `#818594` | `mainBackground` = `#FFF1D1` | 3.28 | 4.5 | `#6A6E7D` → 4.53 ✓ | `#251E0D` → 4.50 ✓ ⚠改动大 | UI 配对 TrialWidget.Active.foreground↔background | 改背景需大幅调整明度，视觉影响大 |
| `error` = `#CE5E5E` | `mainBackground` = `#FFF1D1` | 3.48 | 4.5 | `#C53F3F` → 4.51 ✓ | `#1E180B` → 4.52 ✓ ⚠改动大 | 语义文本 error | 改背景需大幅调整明度，视觉影响大 |
| `info` = `#397FB7` | `mainBackground` = `#FFF1D1` | 3.83 | 4.5 | `#3673A4` → 4.52 ✓ | `#100D06` → 4.52 ✓ ⚠改动大 | 语义文本 info | 改背景需大幅调整明度，视觉影响大 |
| `errorFocused` = `#CB4848` | `mainBackground` = `#FFF1D1` | 4.11 | 4.5 | `#C83C3C` → 4.50 ✓ | `#FDFDFD` → 4.53 ✓ | 语义文本 errorFocused |  |
| `#A46704`（内联） = `#A46704` | `editorBackground` = `#FFFBE8` | 4.47 | 4.5 | `#825F25` → 5.60 ✓ | `#FCFCFC` → 4.53 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground |  |

### light/bean-green

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `hoverBackground` | `#A9CBA3` | `#466A3F` | 4.51 | 2 | ✓ 可一次替换 |
| `#0B843E（内联）` | `#0B843E` | `#1F683E` | 4.54 | 1 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#626571` | 4.54 | 1 | ✓ 可一次替换 |
| `primaryForeground` | `#1A536E` | `#1C475B` | 4.55 | 4 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `selectionBackground` | `#E2F9E1` | `#1A536E`、`#A9CBA3` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `editorBackground` | `#C7EDCC` | `#1A536E`、`#727684` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#A9CBA3` | `#50fa7b`（内联） = `#50FA7B` | 1.30 | 4.5 | `#466A3F` → 4.51 ✓ | `#1C5B2C` → 4.55 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#A9CBA3` | `selectionBackground` = `#E2F9E1` | 1.60 | 4.5 | `#466A3F` → 5.56 ✓ | `#1F5C1C` → 4.51 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `#0B843E`（内联） = `#0B843E` | `tabColor` = `#B8DDBD` | 3.22 | 4.5 | `#1F683E` → 4.54 ✓ | `#F5FAF6` → 4.54 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground |  |
| `#727684`（内联） = `#727684` | `editorBackground` = `#C7EDCC` | 3.54 | 4.5 | `#626571` → 4.54 ✓ | `#FFFFFF` → 4.53 ✓ | UI 配对 TrialWidget.Active.foreground↔background |  |
| `primaryForeground` = `#1A536E` | `#2fc864`（内联） = `#2FC864` | 3.82 | 4.5 | `#1C475B` → 4.55 ✓ | `#52D680` → 4.50 ✓ | UI 配对 Counter.foreground↔background |  |

### light/buff

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `primaryForeground` | `#333333` | — | 4.38 | 3 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `accentColor` | `#BE9B00` | `#726421` | 4.50 | 2 | ✓ 可一次替换 |
| `hoverBackground` | `#DFE1E5` | `#4C515B` | 4.52 | 2 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#6D717E` | 4.53 | 1 | ✓ 可一次替换 |
| `#BE9B00（内联）` | `#BE9B00` | `#736422` | 4.56 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `selectionBackground` | `#DFE1E5` | `#333333`、`#BE9B00`、`#DFE1E5` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `menuBackground` | `#F7F7F2` | `#333333`、`#BE9B00` | 2 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `editorBackground` | `#F7F7F2` | `#333333`、`#727684` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#DFE1E5` | `selectionBackground` = `#DFE1E5` | 1.00 | 4.5 | `#4C515B` → 6.09 ✓ | `#5D6471` → 4.55 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#DFE1E5` | `#c4c3bf`（内联） = `#C4C3BF` | 1.35 | 4.5 | `#4C515B` → 4.52 ✓ | `#66645E` → 4.52 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `accentColor` = `#BE9B00` | `selectionBackground` = `#DFE1E5` | 2.04 | 4.5 | `#726421` → 4.50 ✓ | `#33363D` → 4.54 ✓ ⚠改动大 | UI 配对 TrialWidget.Active.hoverForeground↔hoverBackground | 改背景需大幅调整明度，视觉影响大 |
| `accentColor` = `#BE9B00` | `menuBackground` = `#F7F7F2` | 2.48 | 4.5 | `#726421` → 5.49 ✓ | `#373725` → 4.54 ✓ ⚠改动大 | UI 配对 Plugins.Button.installForeground↔installBackground | 改背景需大幅调整明度，视觉影响大 |
| `#BE9B00`（内联） = `#BE9B00` | `tabColor` = `#E3E3DE` | 2.07 | 4.5 | `#736422` → 4.56 ✓ | `#363630` → 4.56 ✓ ⚠改动大 | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground | 改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#333333` | `#0B843E`（内联） = `#0B843E` | 2.64 | 4.5 | `#F8F8F8` → 4.51 ✓ | `#2CB064` → 4.51 ✓ | UI 配对 Counter.foreground↔background |  |
| `#727684`（内联） = `#727684` | `editorBackground` = `#F7F7F2` | 4.21 | 4.5 | `#6D717E` → 4.53 ✓ | `#FFFFFF` → 4.53 ✓ | UI 配对 TrialWidget.Active.foreground↔background |  |

### light/celadon

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `primaryForeground` | `#1A536E` | `#1C475B` | 4.55 | 3 | ✓ 可一次替换 |
| `#0B843E（内联）` | `#0B843E` | `#206D41` | 4.56 | 1 | ✓ 可一次替换 |
| `hoverBackground` | `#C7DAE1` | `#3F626F` | 4.56 | 2 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#666975` | 4.57 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `selectionBackground` | `#C7DAE1` | `#1A536E`、`#C7DAE1` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `editorBackground` | `#E4ECEF` | `#1A536E`、`#727684` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#C7DAE1` | `selectionBackground` = `#C7DAE1` | 1.00 | 4.5 | `#3F626F` → 4.56 ✓ | `#3F626F` → 4.56 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#C7DAE1` | `#50fa7b`（内联） = `#50FA7B` | 1.05 | 4.5 | `#3F626F` → 4.80 ✓ | `#206B33` → 4.53 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `#0B843E`（内联） = `#0B843E` | `tabColor` = `#CEDEE2` | 3.46 | 4.5 | `#206D41` → 4.56 ✓ | `#F8F8F8` → 4.51 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground |  |
| `#727684`（内联） = `#727684` | `editorBackground` = `#E4ECEF` | 3.78 | 4.5 | `#666975` → 4.57 ✓ | `#FFFFFF` → 4.53 ✓ | UI 配对 TrialWidget.Active.foreground↔background |  |
| `primaryForeground` = `#1A536E` | `#2fc864`（内联） = `#2FC864` | 3.82 | 4.5 | `#1C475B` → 4.55 ✓ | `#52D680` → 4.50 ✓ | UI 配对 Counter.foreground↔background |  |

### light/green-contrast

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `hoverBackground` | `#A3CED0` | `#22393A` | 4.52 | 2 | ✓ 可一次替换 |
| `primaryForeground` | `#1A536E` | `#1C475B` | 4.55 | 4 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#636672` | 4.56 | 1 | ✓ 可一次替换 |
| `#0B843E（内联）` | `#0B843E` | `#216E41` | 5.47 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `selectionBackground` | `#A3CED0` | `#1A536E`、`#A3CED0` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `menuBackground` | `#D0EBEA` | `#1A536E`、`#727684` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#A3CED0` | `selectionBackground` = `#A3CED0` | 1.00 | 4.5 | `#22393A` → 7.17 ✓ | `#33595B` → 4.52 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#A3CED0` | `lineColor` = `#70A6AE` | 1.59 | 4.5 | `#22393A` → 4.52 ✓ | `#38585D` → 4.52 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `#727684`（内联） = `#727684` | `menuBackground` = `#D0EBEA` | 3.61 | 4.5 | `#636672` → 4.56 ✓ | `#FFFFFF` → 4.53 ✓ | UI 配对 TrialWidget.Active.foreground↔background |  |
| `primaryForeground` = `#1A536E` | `#2fc864`（内联） = `#2FC864` | 3.82 | 4.5 | `#1C475B` → 4.55 ✓ | `#52D680` → 4.50 ✓ | UI 配对 Counter.foreground↔background |  |
| `#0B843E`（内联） = `#0B843E` | `tabColor` = `#E4F3F6` | 4.21 | 4.5 | `#216E41` → 5.47 ✓ | `#F3F9FA` → 4.50 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground |  |

### light/green

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `#727684（内联）` | `#727684` | `#696C79` | 4.51 | 1 | ✓ 可一次替换 |
| `hoverBackground` | `#C6E1E0` | `#3B6867` | 4.54 | 2 | ✓ 可一次替换 |
| `primaryForeground` | `#1A536E` | `#1C475B` | 4.55 | 3 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `selectionBackground` | `#C6E1E0` | `#1A536E`、`#C6E1E0` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `editorBackground` | `#E0F2F1` | `#1A536E`、`#727684` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#C6E1E0` | `#50fa7b`（内联） = `#50FA7B` | 1.00 | 4.5 | `#3B6867` → 4.55 ✓ | `#216E34` → 4.56 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#C6E1E0` | `selectionBackground` = `#C6E1E0` | 1.00 | 4.5 | `#3B6867` → 4.54 ✓ | `#3B6867` → 4.54 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#1A536E` | `#2fc864`（内联） = `#2FC864` | 3.82 | 4.5 | `#1C475B` → 4.55 ✓ | `#52D680` → 4.50 ✓ | UI 配对 Counter.foreground↔background |  |
| `#727684`（内联） = `#727684` | `editorBackground` = `#E0F2F1` | 3.91 | 4.5 | `#696C79` → 4.51 ✓ | `#FFFFFF` → 4.53 ✓ | UI 配对 TrialWidget.Active.foreground↔background |  |

### light/grey

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `hoverBackground` | `#DFE1E5` | `#2E3137` | 4.50 | 2 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#6C6F7C` | 4.51 | 1 | ✓ 可一次替换 |
| `primaryForeground` | `#1A536E` | `#1C475B` | 4.55 | 3 | ✓ 可一次替换 |
| `#0B843E（内联）` | `#0B843E` | `#216E41` | 4.76 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `selectionBackground` | `#DFE1E5` | `#1A536E`、`#DFE1E5` | 2 | ✗ 冲突，无单一值可同时满足 |  |
| `editorBackground` | `#F0F4F4` | `#1A536E`、`#727684` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#DFE1E5` | `selectionBackground` = `#DFE1E5` | 1.00 | 4.5 | `#2E3137` → 9.96 ✓ | `#5D6471` → 4.55 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#DFE1E5` | `lineColor` = `#739FA8` | 2.21 | 4.5 | `#2E3137` → 4.50 ✓ | `#486970` → 4.54 ✓ | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核 |
| `#0B843E`（内联） = `#0B843E` | `tabColor` = `#DFE1E5` | 3.66 | 4.5 | `#216E41` → 4.76 ✓ | `#F8F8F8` → 4.51 ✓ | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground |  |
| `primaryForeground` = `#1A536E` | `#2fc864`（内联） = `#2FC864` | 3.82 | 4.5 | `#1C475B` → 4.55 ✓ | `#52D680` → 4.50 ✓ | UI 配对 Counter.foreground↔background |  |
| `#727684`（内联） = `#727684` | `editorBackground` = `#F0F4F4` | 4.08 | 4.5 | `#6C6F7C` → 4.51 ✓ | `#FFFFFF` → 4.53 ✓ | UI 配对 TrialWidget.Active.foreground↔background |  |

### light/pink

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `hoverBackground` | `#FFD6E0` | `#A32A48` | 4.51 | 2 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#6D717E` | 4.54 | 1 | ✓ 可一次替换 |
| `#DB58B0（内联）` | `#DB58B0` | `#C52F94` | 4.57 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `selectionBackground` | `#FFD6E0` | `#3D0013`、`#964A6E`、`#FFD6E0` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `editorBackground` | `#FDF5F6` | `#3D0013`、`#727684` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |
| `menuBackground` | `#FDF5F6` | `#3D0013`、`#964A6E` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#FFD6E0` | `selectionBackground` = `#FFD6E0` | 1.00 | 4.5 | `#A32A48` → 5.35 ✓ | `#B82E4F` → 4.50 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#FFD6E0` | `#ffbdd2`（内联） = `#FFBDD2` | 1.18 | 4.5 | `#A32A48` → 4.51 ✓ | `#B72D59` → 4.51 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `#DB58B0`（内联） = `#DB58B0` | `tabColor` = `#FFF2F0` | 3.18 | 4.5 | `#C52F94` → 4.57 ✓ | `#3B1914` → 4.52 ✓ ⚠改动大 | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground | 改背景需大幅调整明度，视觉影响大 |
| `#727684`（内联） = `#727684` | `editorBackground` = `#FDF5F6` | 4.22 | 4.5 | `#6D717E` → 4.54 ✓ | `#FFFFFF` → 4.53 ✓ | UI 配对 TrialWidget.Active.foreground↔background |  |

### light/yellow

#### 全局统一建议值

| 前景 token | 原色值 | 统一建议值 | 建议后最低对比度 | 覆盖底色数 | 状态 |
|---|---|---|---|---|---|
| `primaryForeground` | `#333333` | — | 4.38 | 3 | ✗ 无统一值，需按使用点单独设定（见下表） |
| `hoverBackground` | `#CCC8B6` | `#595440` | 4.52 | 1 | ✓ 可一次替换 |
| `accentColor` | `#BE9B00` | `#60531D` | 4.54 | 2 | ✓ 可一次替换 |
| `#BE9B00（内联）` | `#BE9B00` | `#796923` | 4.55 | 1 | ✓ 可一次替换 |
| `#727684（内联）` | `#727684` | `#6F7380` | 4.55 | 1 | ✓ 可一次替换 |

#### 背景侧诊断

下表列出**被多个前景色共用**的背景 token —— 这类背景一旦改动会连带影响多处，因此本表只做诊断（是否冲突、可否一次替换），不作为直接的改动建议；背景侧的取值请看「逐配对明细」的「改背景建议」列。

| 背景 token | 原色值 | 共用前景色 | 前景数 | 一次替换可行性 | 说明 |
|---|---|---|---|---|---|
| `selectionBackground` | `#CCC8B6` | `#333333`、`#BE9B00`、`#CCC8B6` | 3 | ✗ 冲突，无单一值可同时满足 |  |
| `menuBackground` | `#FFFBE8` | `#333333`、`#BE9B00` | 2 | ✗ 冲突，无单一值可同时满足 | 核心底色，改动影响全局观感 |
| `editorBackground` | `#FFFBE8` | `#333333`、`#727684` | 2 | ✓ 可一次替换 | 核心底色，改动影响全局观感 |

#### 逐配对明细

`改前景建议 ★` = 推荐方案。当该前景 token 的「全局统一建议值」可行时，直接替换 token 值即可（取值为统一建议值）；当统一值不可行时，该 token 需**按使用点内联单独色值**（取值为本配对专属值）。`改背景建议（备选）` = 若不愿改前景，可改对应背景色，但需确认该背景未被其他场景复用。

| 前景（token） | 背景（token） | 原对比度 | 目标 | 改前景建议 ★ | 改背景建议（备选） | 用途 | 备注 |
|---|---|---|---|---|---|---|---|
| `hoverBackground` = `#CCC8B6` | `#ccc8b6`（内联） = `#CCC8B6` | 1.00 | 4.5 | `#595440` → 4.52 ✓ | `#595440` → 4.52 ✓ ⚠改动大 | UI 配对 ToggleButton.onForeground↔onBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `hoverBackground` = `#CCC8B6` | `selectionBackground` = `#CCC8B6` | 1.00 | 4.5 | `#595440` → 4.52 ✓ | `#595440` → 4.52 ✓ ⚠改动大 | UI 配对 ToggleButton.offForeground↔offBackground | 该 token 同时用作背景，替换需同步复核；改背景需大幅调整明度，视觉影响大 |
| `accentColor` = `#BE9B00` | `selectionBackground` = `#CCC8B6` | 1.59 | 4.5 | `#60531D` → 4.54 ✓ | `#39362A` → 4.54 ✓ ⚠改动大 | UI 配对 TrialWidget.Active.hoverForeground↔hoverBackground | 改背景需大幅调整明度，视觉影响大 |
| `accentColor` = `#BE9B00` | `menuBackground` = `#FFFBE8` | 2.57 | 4.5 | `#60531D` → 7.34 ✓ | `#3D3614` → 4.54 ✓ ⚠改动大 | UI 配对 Plugins.Button.installForeground↔installBackground | 改背景需大幅调整明度，视觉影响大 |
| `#BE9B00`（内联） = `#BE9B00` | `tabColor` = `#EFEBD8` | 2.23 | 4.5 | `#796923` → 4.55 ✓ | `#3B361D` → 4.55 ✓ ⚠改动大 | UI 配对 MainWindow.Tab.selectedForeground↔selectedBackground | 改背景需大幅调整明度，视觉影响大 |
| `primaryForeground` = `#333333` | `#0B843E`（内联） = `#0B843E` | 2.64 | 4.5 | `#F8F8F8` → 4.51 ✓ | `#2CB064` → 4.51 ✓ | UI 配对 Counter.foreground↔background |  |
| `#727684`（内联） = `#727684` | `editorBackground` = `#FFFBE8` | 4.36 | 4.5 | `#6F7380` → 4.55 ✓ | `#FFFFFF` → 4.53 ✓ | UI 配对 TrialWidget.Active.foreground↔background |  |

## 四、编辑器配色方案 XML 不达标项对照表

说明：

- `背景` 列为该前景色实际渲染时所处的底色。标 `自带背景` 的属性自带 `BACKGROUND`，需**单独定值**，不能全局替换；
- 其余条目的前景色渲染在方案背景上，替换建议值即可全局生效；
- `影响属性数` 指该色值在方案中被多少个语法属性引用；
- `ANSI 语义色` 为控制台 ANSI 固定色映射，属语义约定，仅提示不建议修改；
- `ANSI 混用` 指该色值在同一底色下**既有**普通语法属性**又有** `CONSOLE_*` 控制台属性 —— 语法侧必须改色才能达标，但改色会连带改变控制台输出配色，需人工确认后再改；
- `建议值 ★` = 推荐采用：**本方案统一值**（该色值在本方案内所有底色上一次性达标，替换后全局生效）；若本方案内无统一值（同一色值被用在明暗差异极大的底色上），则退化为**按使用点单独定值**，取值为该行专属建议值；
- `改背景建议（备选）` = 若不愿改前景，可调整该行的底色。

### dark/dark-blue.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#21222C` | `#233348` | 1.23 | 4.5 | 保持原值 → 1.23 — | `#6B8CB7` → 4.56 | 1 | CONSOLE_BLACK_OUTPUT | ANSI 语义色·不建议修改；保持原值 |
| `#374357` | `#233348` | 1.28 | 3.0 | `#677C9F` → 3.03 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#005452` | `#233348` | 1.46 | 3.0 | `#268885` → 3.02 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#969696` | `#4D5C74` | 2.29 | 4.5 | `#D3D3D3` → 4.53 ✓ | `#292F39` → 4.55 | 1 | FOLDED_TEXT_ATTRIBUTES | 自带背景·单独定值；本方案统一值 |
| `#6272A4` | `#233348` | 2.72 | 4.5 | `#8D99BD` → 4.53 ✓ | `#FAFAFA` → 4.51 | 9 | CONSOLE_DARKGRAY_OUTPUT, DEFAULT_LABEL, KOTLIN_LABEL | ANSI 混用：同色值下 1/9 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#CD4644` | `#233348` | 2.78 | 3.0 | `#D0504E` → 3.01 ✓ | — | 1 | <colors>DELETED_LINES_COLOR |  |
| `#6F7587` | `#233348` | 2.79 | 4.5 | `#9499A7` → 4.50 ✓ | `#020202` → 4.52 | 1 | REGEXP.REDUNDANT_ESCAPE | 本方案统一值 |
| `#CD4848` | `#233348` | 2.82 | 4.5 | `#DC7F7F` → 4.51 ✓ | `#040404` → 4.51 | 1 | UNMATCHED_BRACE_ATTRIBUTES | 本方案统一值 |
| `#B2CDE2` | `#007F5F` | 3.03 | 4.5 | `#EFF4F8` → 4.51 ✓ | `#1D5F4E` → 4.55 | 1 | TEXT_SEARCH_RESULT_ATTRIBUTES | 自带背景·单独定值；本方案统一值 |
| `#9876AA` | `#233348` | 3.36 | 4.5 | `#AC90BA` → 4.55 ✓ | `#151C24` → 4.50 | 91 | BREADCRUMBS_CURRENT, BREADCRUMBS_DEFAULT, COFFEESCRIPT.FUNCTION_NAME | ANSI 混用：同色值下 3/91 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#969696` | `#344359` | 3.39 | 4.5 | `#D3D3D3` → 6.70 ✓ | `#27303D` → 4.50 | 1 | INLINE_PARAMETER_HINT | 自带背景·单独定值；本方案统一值 |
| `#1D9A62` | `#233348` | 3.57 | 4.5 | `#2CAE74` → 4.52 ✓ | `#19212C` → 4.52 | 25 | CONSOLE_MAGENTA_OUTPUT, CSS.TAG_NAME, CUSTOM_KEYWORD1_ATTRIBUTES | ANSI 混用：同色值下 1/25 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#F8F8F2` | `#747A9D` | 3.93 | 4.5 | `#11110B` → 4.52 ✓ | `#697096` → 4.52 | 1 | MATCHED_BRACE_ATTRIBUTES | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#9876AA` | `#242632` | 3.94 | 4.5 | 保持原值 → 3.94 — | `#1A1B21` → 4.50 | 1 | CONSOLE_SELECTED_PARAMETER | ANSI 语义色·不建议修改；自带背景·单独定值；保持原值 |
| `#1D9A62` | `#282A36` | 3.97 | 4.5 | `#2CAE74` → 5.03 ✓ | `#1F2027` → 4.52 | 1 | BREADCRUMBS_HOVERED | 自带背景·单独定值；本方案统一值 |
| `#FF5555` | `#233348` | 4.08 | 4.5 | `#E07D7D` → 4.52 ✓ | `#202C3A` → 4.51 | 7 | CONSOLE_ERROR_OUTPUT, CONSOLE_RED_OUTPUT, DEFAULT_INVALID_STRING_ESCAPE | ANSI 混用：同色值下 2/7 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#C58564` | `#233348` | 4.22 | 4.5 | `#C88C6C` → 4.53 ✓ | `#222E3D` → 4.53 | 24 | Anotation attribute name, CSS.URL, CUSTOM_VALID_STRING_ESCAPE_ATTRIBUTES | 本方案统一值 |
| `#A09A1D` | `#233348` | 4.35 | 4.5 | `#A39D2A` → 4.52 ✓ | `#243041` → 4.53 | 1 | Annotation | 本方案统一值 |

### dark/dark-coffee.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#21222C` | `#2C2122` | 1.01 | 4.5 | 保持原值 → 1.01 — | `#A18285` → 4.55 | 1 | CONSOLE_BLACK_OUTPUT | ANSI 语义色·不建议修改；保持原值 |
| `#413334` | `#2C2122` | 1.30 | 3.0 | `#826668` → 3.00 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#554849` | `#2C2122` | 1.79 | 3.0 | `#7B696A` → 3.02 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#8D4532` | `#2C2122` | 2.25 | 4.5 | `#C6755F` → 4.53 ✓ | `#D9CECF` → 4.52 | 2 | XML_TAG, XML_TAG_NAME | 本方案统一值 |
| `#15845D` | `#282A36` | 3.05 | 4.5 | `#2BA479` → 4.54 ✓ | `#FBFBFB` → 4.52 | 1 | BREADCRUMBS_HOVERED | 自带背景·单独定值；本方案统一值 |
| `#6272A4` | `#2C2122` | 3.31 | 4.5 | `#7B89B3` → 4.50 ✓ | `#FAFAFA` → 4.51 | 9 | CONSOLE_DARKGRAY_OUTPUT, DEFAULT_LABEL, KOTLIN_LABEL | ANSI 混用：同色值下 1/9 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#15845D` | `#2C2122` | 3.33 | 4.5 | `#2BA479` → 4.96 ✓ | `#FBFBFB` → 4.52 | 23 | CONSOLE_MAGENTA_OUTPUT, CSS.TAG_NAME, CUSTOM_KEYWORD1_ATTRIBUTES | ANSI 混用：同色值下 1/23 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#969696` | `#344359` | 3.39 | 4.5 | `#AEAEAE` → 4.52 ✓ | `#27303D` → 4.50 | 1 | INLINE_PARAMETER_HINT | 自带背景·单独定值；本方案统一值 |
| `#6F7587` | `#2C2122` | 3.39 | 4.5 | `#858A9A` → 4.53 ✓ | `#020202` → 4.52 | 1 | REGEXP.REDUNDANT_ESCAPE | 本方案统一值 |
| `#CD4848` | `#2C2122` | 3.43 | 4.5 | `#D66969` → 4.51 ✓ | `#040404` → 4.51 | 1 | UNMATCHED_BRACE_ATTRIBUTES | 本方案统一值 |
| `#BFA5A2` | `#6E4552` | 3.46 | 4.5 | `#D1BFBD` → 4.51 ✓ | `#543841` → 4.52 | 1 | TEXT_SEARCH_RESULT_ATTRIBUTES | 自带背景·单独定值；本方案统一值 |
| `#9876AA` | `#242632` | 3.94 | 4.5 | 保持原值 → 3.94 — | `#1A1B21` → 4.50 | 1 | CONSOLE_SELECTED_PARAMETER | ANSI 语义色·不建议修改；自带背景·单独定值；保持原值 |
| `#969696` | `#413334` | 4.06 | 4.5 | `#AEAEAE` → 5.42 ✓ | `#372D2E` → 4.50 | 1 | FOLDED_TEXT_ATTRIBUTES | 自带背景·单独定值；本方案统一值 |
| `#9876AA` | `#2C2122` | 4.09 | 4.5 | `#A182B1` → 4.71 ✓ | `#20191A` → 4.53 | 91 | BREADCRUMBS_CURRENT, BREADCRUMBS_DEFAULT, COFFEESCRIPT.FUNCTION_NAME | ANSI 混用：同色值下 3/91 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |

### dark/dark-darcula.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#4C5052` | `#3C3F41` | 1.30 | 3.0 | `#848A8D` → 3.03 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#544E6B` | `#3C3F41` | 1.35 | 3.0 | `#8B84A5` → 3.00 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#21222C` | `#3C3F41` | 1.49 | 4.5 | 保持原值 → 1.49 — | `#848A8E` → 4.52 | 1 | CONSOLE_BLACK_OUTPUT | ANSI 语义色·不建议修改；保持原值 |
| `#6272A4` | `#3C3F41` | 2.25 | 4.5 | `#9FA9C7` → 4.53 ✓ | `#FAFAFA` → 4.51 | 9 | CONSOLE_DARKGRAY_OUTPUT, DEFAULT_LABEL, KOTLIN_LABEL | ANSI 混用：同色值下 1/9 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#CD4644` | `#3C3F41` | 2.30 | 3.0 | `#D66664` → 3.00 ✓ | — | 1 | <colors>DELETED_LINES_COLOR |  |
| `#6F7587` | `#3C3F41` | 2.31 | 4.5 | `#A5A9B5` → 4.52 ✓ | `#020202` → 4.52 | 1 | REGEXP.REDUNDANT_ESCAPE | 本方案统一值 |
| `#CD4848` | `#3C3F41` | 2.33 | 4.5 | `#E29595` → 4.53 ✓ | `#040404` → 4.51 | 1 | UNMATCHED_BRACE_ATTRIBUTES | 本方案统一值 |
| `#969696` | `#4C5052` | 2.76 | 4.5 | `#C1C1C1` → 4.53 ✓ | `#2D2F30` → 4.55 | 2 | FOLDED_TEXT_ATTRIBUTES, INLINE_PARAMETER_HINT | 自带背景·单独定值；本方案统一值 |
| `#9876AA` | `#3C3F41` | 2.78 | 4.5 | `#B8A1C4` → 4.51 ✓ | `#1A1B1C` → 4.52 | 92 | BREADCRUMBS_CURRENT, BREADCRUMBS_DEFAULT, COFFEESCRIPT.FUNCTION_NAME | ANSI 混用：同色值下 3/92 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#1D9A62` | `#3C3F41` | 2.96 | 4.5 | `#2FC07F` → 4.53 ✓ | `#1F2122` → 4.50 | 25 | CONSOLE_MAGENTA_OUTPUT, CSS.TAG_NAME, CUSTOM_KEYWORD1_ATTRIBUTES | ANSI 混用：同色值下 1/25 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#FF5555` | `#3C3F41` | 3.38 | 4.5 | `#E59393` → 4.51 ✓ | `#292B2C` → 4.53 | 7 | CONSOLE_ERROR_OUTPUT, CONSOLE_RED_OUTPUT, DEFAULT_INVALID_STRING_ESCAPE | ANSI 混用：同色值下 2/7 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#B2CDE2` | `#6A618C` | 3.44 | 4.5 | `#DCE7EF` → 4.52 ✓ | `#575171` → 4.51 | 1 | TEXT_SEARCH_RESULT_ATTRIBUTES | 自带背景·单独定值；本方案统一值 |
| `#C58564` | `#3C3F41` | 3.49 | 4.5 | `#D19E84` → 4.51 ✓ | `#2B2D2E` → 4.56 | 24 | Anotation attribute name, CSS.URL, CUSTOM_VALID_STRING_ESCAPE_ATTRIBUTES | 本方案统一值 |
| `#A09A1D` | `#3C3F41` | 3.60 | 4.5 | `#B3AD2D` → 4.51 ✓ | `#2E3031` → 4.51 | 1 | Annotation | 本方案统一值 |
| `#85A658` | `#3C3F41` | 3.84 | 4.5 | `#96B36F` → 4.53 ✓ | `#323435` → 4.52 | 5 | DEFAULT_STATIC_FIELD, DEFAULT_STATIC_METHOD, Static method access | 本方案统一值 |
| `#6AAB73` | `#3C3F41` | 3.88 | 4.5 | `#7EB686` → 4.51 ✓ | `#333537` → 4.51 | 9 | DEFAULT_STRING, GO_STRING, JS.VALID_STRING_ESCAPE | 本方案统一值；色盲变体需同步:94907F/94907F |
| `#FF6E6E` | `#3C3F41` | 3.89 | 4.5 | 保持原值 → 3.89 — | `#333537` → 4.52 | 1 | CONSOLE_RED_BRIGHT_OUTPUT | ANSI 语义色·不建议修改；保持原值 |
| `#9876AA` | `#242632` | 3.94 | 4.5 | 保持原值 → 3.94 — | `#1A1B21` → 4.50 | 1 | CONSOLE_SELECTED_PARAMETER | ANSI 语义色·不建议修改；自带背景·单独定值；保持原值 |
| `#1D9A62` | `#282A36` | 3.97 | 4.5 | `#2FC07F` → 6.08 ✓ | `#1F2027` → 4.52 | 1 | BREADCRUMBS_HOVERED | 自带背景·单独定值；本方案统一值 |
| `#00B9AF` | `#3C3F41` | 4.32 | 4.5 | `#2EBCB5` → 4.53 ✓ | `#3A3C3E` → 4.51 | 1 | DEFAULT_PARAMETER | 本方案统一值 |

### dark/dark-green.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#21222C` | `#002F38` | 1.10 | 4.5 | 保持原值 → 1.10 — | `#2C96AB` → 4.56 | 1 | CONSOLE_BLACK_OUTPUT | ANSI 语义色·不建议修改；保持原值 |
| `#153F48` | `#002F38` | 1.25 | 3.0 | `#2E7D8E` → 3.03 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#305862` | `#002F38` | 1.84 | 3.0 | `#457B88` → 3.03 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#969696` | `#305862` | 2.63 | 4.5 | `#C5C5C5` → 4.51 ✓ | `#1F3237` → 4.52 | 1 | FOLDED_TEXT_ATTRIBUTES | 自带背景·单独定值；本方案统一值 |
| `#B2CDE2` | `#007F5F` | 3.03 | 4.5 | `#EFF4F8` → 4.51 ✓ | `#1D5F4E` → 4.55 | 1 | TEXT_SEARCH_RESULT_ATTRIBUTES | 自带背景·单独定值；本方案统一值 |
| `#6272A4` | `#002F38` | 3.05 | 4.5 | `#8490B7` → 4.55 ✓ | `#FAFAFA` → 4.51 | 9 | CONSOLE_DARKGRAY_OUTPUT, DEFAULT_LABEL, KOTLIN_LABEL | ANSI 混用：同色值下 1/9 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#6F7587` | `#002F38` | 3.12 | 4.5 | `#8B909F` → 4.50 ✓ | `#020202` → 4.52 | 1 | REGEXP.REDUNDANT_ESCAPE | 本方案统一值 |
| `#CD4848` | `#002F38` | 3.15 | 4.5 | `#D97373` → 4.54 ✓ | `#040404` → 4.51 | 1 | UNMATCHED_BRACE_ATTRIBUTES | 本方案统一值 |
| `#9876AA` | `#002F38` | 3.76 | 4.5 | `#A486B4` → 4.54 ✓ | `#0C1E21` → 4.50 | 91 | BREADCRUMBS_CURRENT, BREADCRUMBS_DEFAULT, COFFEESCRIPT.FUNCTION_NAME | ANSI 混用：同色值下 3/91 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#969696` | `#153F48` | 3.87 | 4.5 | `#C5C5C5` → 6.63 ✓ | `#163339` → 4.53 | 1 | INLINE_PARAMETER_HINT | 自带背景·单独定值；本方案统一值 |
| `#9876AA` | `#242632` | 3.94 | 4.5 | 保持原值 → 3.94 — | `#1A1B21` → 4.50 | 1 | CONSOLE_SELECTED_PARAMETER | ANSI 语义色·不建议修改；自带背景·单独定值；保持原值 |
| `#1D9A62` | `#282A36` | 3.97 | 4.5 | `#2BA46E` → 4.50 ✓ | `#1F2027` → 4.52 | 1 | BREADCRUMBS_HOVERED | 自带背景·单独定值；本方案统一值 |
| `#F8F8F2` | `#00898A` | 3.98 | 4.5 | `#0F0F09` → 4.53 ✓ | `#20696A` → 5.99 | 1 | MATCHED_BRACE_ATTRIBUTES | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#1D9A62` | `#002F38` | 4.00 | 4.5 | `#2BA46E` → 4.54 ✓ | `#0E2327` → 4.54 | 25 | CONSOLE_MAGENTA_OUTPUT, CSS.TAG_NAME, CUSTOM_KEYWORD1_ATTRIBUTES | ANSI 混用：同色值下 1/25 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |

### dark/dark-violet.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#21222C` | `#2B2640` | 1.09 | 4.5 | 保持原值 → 1.09 — | `#8D83B5` → 4.55 | 1 | CONSOLE_BLACK_OUTPUT | ANSI 语义色·不建议修改；保持原值 |
| `#433D5C` | `#2B2640` | 1.42 | 3.0 | `#756C9D` → 3.02 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#544E6B` | `#2B2640` | 1.84 | 3.0 | `#756D95` → 3.01 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#6272A4` | `#2B2640` | 3.07 | 4.5 | `#828FB6` → 4.51 ✓ | `#FAFAFA` → 4.51 | 9 | CONSOLE_DARKGRAY_OUTPUT, DEFAULT_LABEL, KOTLIN_LABEL | ANSI 混用：同色值下 1/9 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#6F7587` | `#2B2640` | 3.14 | 4.5 | `#8B909F` → 4.53 ✓ | `#020202` → 4.52 | 1 | REGEXP.REDUNDANT_ESCAPE | 本方案统一值 |
| `#CD4848` | `#2B2640` | 3.17 | 4.5 | `#D87272` → 4.52 ✓ | `#040404` → 4.51 | 1 | UNMATCHED_BRACE_ATTRIBUTES | 本方案统一值 |
| `#B2CDE2` | `#6A618C` | 3.44 | 4.5 | `#DCE7EF` → 4.52 ✓ | `#575171` → 4.51 | 1 | TEXT_SEARCH_RESULT_ATTRIBUTES | 自带背景·单独定值；本方案统一值 |
| `#969696` | `#433D5C` | 3.45 | 4.5 | `#ADADAD` → 4.54 ✓ | `#302C3F` → 4.56 | 2 | FOLDED_TEXT_ATTRIBUTES, INLINE_PARAMETER_HINT | 自带背景·单独定值；本方案统一值 |
| `#9876AA` | `#2B2640` | 3.79 | 4.5 | `#A385B3` → 4.51 ✓ | `#1C1926` → 4.53 | 92 | BREADCRUMBS_CURRENT, BREADCRUMBS_DEFAULT, COFFEESCRIPT.FUNCTION_NAME | ANSI 混用：同色值下 3/92 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#9876AA` | `#242632` | 3.94 | 4.5 | 保持原值 → 3.94 — | `#1A1B21` → 4.50 | 1 | CONSOLE_SELECTED_PARAMETER | ANSI 语义色·不建议修改；自带背景·单独定值；保持原值 |
| `#1D9A62` | `#282A36` | 3.97 | 4.5 | `#2BA46E` → 4.50 ✓ | `#1F2027` → 4.52 | 1 | BREADCRUMBS_HOVERED | 自带背景·单独定值；本方案统一值 |
| `#1D9A62` | `#2B2640` | 4.02 | 4.5 | `#2BA46E` → 4.57 ✓ | `#211E2E` → 4.54 | 25 | CONSOLE_MAGENTA_OUTPUT, CSS.TAG_NAME, CUSTOM_KEYWORD1_ATTRIBUTES | ANSI 混用：同色值下 1/25 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |

### dark/dark.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#21222C` | `#282A36` | 1.11 | 4.5 | 保持原值 → 1.11 — | `#8388A3` → 4.52 | 1 | CONSOLE_BLACK_OUTPUT | ANSI 语义色·不建议修改；保持原值 |
| `#3B3D49` | `#282A36` | 1.32 | 3.0 | `#6E7288` → 3.00 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#544E6B` | `#282A36` | 1.82 | 3.0 | `#766E95` → 3.01 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#6272A4` | `#282A36` | 3.03 | 4.5 | `#8490B7` → 4.51 ✓ | `#FAFAFA` → 4.51 | 9 | CONSOLE_DARKGRAY_OUTPUT, DEFAULT_LABEL, KOTLIN_LABEL | ANSI 混用：同色值下 1/9 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#6F7587` | `#282A36` | 3.10 | 4.5 | `#8C91A0` → 4.52 ✓ | `#020202` → 4.52 | 1 | REGEXP.REDUNDANT_ESCAPE | 本方案统一值 |
| `#CD4848` | `#282A36` | 3.13 | 4.5 | `#D97373` → 4.51 ✓ | `#040404` → 4.51 | 1 | UNMATCHED_BRACE_ATTRIBUTES | 本方案统一值 |
| `#B2CDE2` | `#6A618C` | 3.44 | 4.5 | `#DCE7EF` → 4.52 ✓ | `#575171` → 4.51 | 1 | TEXT_SEARCH_RESULT_ATTRIBUTES | 自带背景·单独定值；本方案统一值 |
| `#969696` | `#3B3D49` | 3.64 | 4.5 | `#A8A8A8` → 4.53 ✓ | `#2D2F36` → 4.52 | 2 | FOLDED_TEXT_ATTRIBUTES, INLINE_PARAMETER_HINT | 自带背景·单独定值；本方案统一值 |
| `#9876AA` | `#282A36` | 3.73 | 4.5 | `#A486B4` → 4.51 ✓ | `#1A1B21` → 4.50 | 92 | BREADCRUMBS_CURRENT, BREADCRUMBS_DEFAULT, COFFEESCRIPT.FUNCTION_NAME | ANSI 混用：同色值下 3/92 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |
| `#9876AA` | `#242632` | 3.94 | 4.5 | 保持原值 → 3.94 — | `#1A1B21` → 4.50 | 1 | CONSOLE_SELECTED_PARAMETER | ANSI 语义色·不建议修改；自带背景·单独定值；保持原值 |
| `#1D9A62` | `#282A36` | 3.97 | 4.5 | `#2BA46E` → 4.50 ✓ | `#1F2027` → 4.52 | 1 | BREADCRUMBS_HOVERED | 自带背景·单独定值；本方案统一值 |
| `#1D9A62` | `#282A36` | 3.97 | 4.5 | `#2BA46E` → 4.50 ✓ | `#1F2027` → 4.52 | 25 | CONSOLE_MAGENTA_OUTPUT, CSS.TAG_NAME, CUSTOM_KEYWORD1_ATTRIBUTES | ANSI 混用：同色值下 1/25 个属性为 CONSOLE_*，改色会连带影响控制台配色；本方案统一值 |

### light/bean-green.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#643CB8` | `#933232` | 1.05 | 4.5 | `#CEC1E8` → 4.53 ✓ | `#E8C3C3` → 4.52 | 1 | DELETED_TEXT_ATTRIBUTES | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#E2F9E1` | `#C7EDCC` | 1.15 | 3.0 | `#2D9528` → 3.02 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#AFDBB8` | `#C7EDCC` | 1.20 | 3.0 | `#449154` → 3.03 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#FFB86C` | `#C7EDCC` | 1.33 | 4.5 | `#8C5B27` → 4.51 ✓ | `#245E2C` → 4.54 | 18 | CUSTOM_VALID_STRING_ESCAPE_ATTRIBUTES, GO_BUILTIN_FUNCTION, GO_STRUCT_EXPORTED_MEMBER | 本方案统一值 |
| `#7FC784` | `#C7EDCC` | 1.58 | 3.0 | `#439248` → 3.02 ✓ | — | 1 | <colors>ADDED_LINES_COLOR |  |
| `#643CB8` | `#282A36` | 1.95 | 4.5 | `#9E83D7` → 4.53 ✓ | `#C9CBD6` → 4.51 | 1 | HAML_TAG | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#9C411A` | `#282A36` | 2.16 | 4.5 | `#D6784F` → 4.52 ✓ | `#D4D5DE` → 4.51 | 1 | RUBY_HEREDOC_CONTENT | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#FF5555` | `#C7EDCC` | 2.46 | 4.5 | `#BF2E2E` → 4.50 ✓ | `#153118` → 4.50 | 5 | DEFAULT_INVALID_STRING_ESCAPE, WRONG_REFERENCES_ATTRIBUTES, JUPYTER_INLINE_VALUES | 本方案统一值 |
| `#8C8C8C` | `#C7EDCC` | 2.63 | 3.0 | `#828282` → 3.00 ✓ | `#E8F6EA` → 3.01 | 7 | DEFAULT_LINE_COMMENT, DEFAULT_BLOCK_COMMENT, DEFAULT_DOC_COMMENT | 本方案统一值 |
| `#9E880D` | `#C7EDCC` | 2.74 | 4.5 | `#726621` → 4.51 ✓ | `#112714` → 4.52 | 1 | DEFAULT_METADATA | 本方案统一值；色盲变体需同步:3381FF/3381FF |
| `#A35CF0` | `#C7EDCC` | 3.07 | 4.5 | `#853FD2` → 4.51 ✓ | `#0C1C0E` → 4.50 | 1 | TODO_DEFAULT_ATTRIBUTES | 本方案统一值 |
| `#CD4848` | `#C7EDCC` | 3.56 | 4.5 | `#BA3434` → 4.52 ✓ | `#FEFEFE` → 4.51 | 1 | UNMATCHED_BRACE_ATTRIBUTES | 本方案统一值 |
| `#4E8052` | `#C7EDCC` | 3.62 | 4.5 | `#466F4A` → 4.52 ✓ | `#FCFCFC` → 4.52 | 2 | DEFAULT_DOC_COMMENT_TAG_VALUE, DEFAULT_DOC_MARKUP | 本方案统一值 |
| `#0077C2` | `#C7EDCC` | 3.72 | 4.5 | `#286B96` → 4.52 ✓ | `#F4FBF5` → 4.52 | 4 | INACTIVE_HYPERLINK_ATTRIBUTES, FOLLOWED_HYPERLINK_ATTRIBUTES, HYPERLINK_ATTRIBUTES | 本方案统一值；色盲变体需同步:3777E6/3777E6 |
| `#067D17` | `#C7EDCC` | 4.15 | 4.5 | `#1E6428` → 5.64 ✓ | `#DDF2E0` → 4.52 | 38 | DEFAULT_OPERATION_SIGN, DEFAULT_DOT, DEFAULT_SEMICOLON | 本方案统一值 |
| `#414D41` | `#96BB9B` | 4.18 | 4.5 | `#3D473D` → 4.56 ✓ | `#9FC1A4` → 4.50 | 1 | FOLDED_TEXT_ATTRIBUTES | 自带背景·单独定值；本方案统一值；色盲变体需同步:4d4d4d/4d4d4d |
| `#007297` | `#C7EDCC` | 4.26 | 4.5 | `#226075` → 5.47 ✓ | `#D7F0DB` → 4.51 | 79 | ANNOTATION_ATTRIBUTE_NAME_ATTRIBUTES, ANONYMOUS_CLASS_NAME_ATTRIBUTES, Abstract class name | 本方案统一值 |

### light/buff.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#643CB8` | `#933232` | 1.05 | 4.5 | `#CEC1E8` → 4.53 ✓ | `#E8C3C3` → 4.52 | 1 | DELETED_TEXT_ATTRIBUTES | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#FFE2C1` | `#F7F7F2` | 1.16 | 3.0 | `#C57F2F` → 3.03 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#DFE1E5` | `#F7F7F2` | 1.22 | 3.0 | `#878F9E` → 3.03 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#FFB86C` | `#F7F7F2` | 1.59 | 4.5 | `#9D652A` → 4.51 ✓ | `#545437` → 4.56 | 18 | CUSTOM_VALID_STRING_ESCAPE_ATTRIBUTES, GO_BUILTIN_FUNCTION, GO_STRUCT_EXPORTED_MEMBER | 本方案统一值 |
| `#7FC784` | `#F7F7F2` | 1.88 | 3.0 | `#48A14E` → 3.01 ✓ | — | 1 | <colors>ADDED_LINES_COLOR |  |
| `#643CB8` | `#282A36` | 1.95 | 4.5 | `#9E83D7` → 4.53 ✓ | `#C9CBD6` → 4.51 | 1 | HAML_TAG | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#9C411A` | `#282A36` | 2.16 | 4.5 | `#D6784F` → 4.52 ✓ | `#D4D5DE` → 4.51 | 1 | RUBY_HEREDOC_CONTENT | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#B7A974` | `#F7F7F2` | 2.19 | 4.5 | `#7D7144` → 4.53 ✓ | `#40402B` → 4.50 | 3 | DEFAULT_BRACES, DEFAULT_PARENTHS, DEFAULT_BRACKETS | 本方案统一值 |
| `#FF5555` | `#F7F7F2` | 2.92 | 4.5 | `#D03939` → 4.52 ✓ | `#2B2B1D` → 4.56 | 5 | DEFAULT_INVALID_STRING_ESCAPE, WRONG_REFERENCES_ATTRIBUTES, JUPYTER_INLINE_VALUES | 本方案统一值 |
| `#9E880D` | `#F7F7F2` | 3.27 | 4.5 | `#7F7124` → 4.56 ✓ | `#232318` → 4.51 | 1 | DEFAULT_METADATA | 本方案统一值；色盲变体需同步:9e880d/9e880d |
| `#A35CF0` | `#F7F7F2` | 3.66 | 4.5 | `#9150D6` → 4.52 ✓ | `#181811` → 4.54 | 1 | TODO_DEFAULT_ATTRIBUTES | 本方案统一值 |
| `#4E8052` | `#F7F7F2` | 4.31 | 4.5 | `#4E7C51` → 4.52 ✓ | `#FCFCFC` → 4.52 | 2 | DEFAULT_DOC_COMMENT_TAG_VALUE, DEFAULT_DOC_MARKUP | 本方案统一值 |
| `#0077C2` | `#F7F7F2` | 4.42 | 4.5 | `#296E99` → 5.16 ✓ | `#F9F9F6` → 4.51 | 4 | INACTIVE_HYPERLINK_ATTRIBUTES, FOLLOWED_HYPERLINK_ATTRIBUTES, HYPERLINK_ATTRIBUTES | 本方案统一值；色盲变体需同步:3777E6/3777E6 |

### light/celadon.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#643CB8` | `#933232` | 1.05 | 4.5 | `#CEC1E8` → 4.53 ✓ | `#E8C3C3` → 4.52 | 1 | DELETED_TEXT_ATTRIBUTES | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#F7F8FA` | `#E4ECEF` | 1.13 | 3.0 | `#7687A9` → 3.02 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#AFDBB8` | `#E4ECEF` | 1.29 | 3.0 | `#469757` → 3.01 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#FFB86C` | `#E4ECEF` | 1.42 | 4.5 | `#925F28` → 4.51 ✓ | `#3D5761` → 4.51 | 18 | CUSTOM_VALID_STRING_ESCAPE_ATTRIBUTES, GO_BUILTIN_FUNCTION, GO_STRUCT_EXPORTED_MEMBER | 本方案统一值 |
| `#7FC784` | `#E4ECEF` | 1.69 | 3.0 | `#45984B` → 3.00 ✓ | — | 1 | <colors>ADDED_LINES_COLOR |  |
| `#643CB8` | `#282A36` | 1.95 | 4.5 | `#9E83D7` → 4.53 ✓ | `#C9CBD6` → 4.51 | 1 | HAML_TAG | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#9C411A` | `#282A36` | 2.16 | 4.5 | `#D6784F` → 4.52 ✓ | `#D4D5DE` → 4.51 | 1 | RUBY_HEREDOC_CONTENT | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#FF5555` | `#E4ECEF` | 2.63 | 4.5 | `#C73030` → 4.51 ✓ | `#202D31` → 4.51 | 5 | DEFAULT_INVALID_STRING_ESCAPE, WRONG_REFERENCES_ATTRIBUTES, JUPYTER_INLINE_VALUES | 本方案统一值 |
| `#8C8C8C` | `#E4ECEF` | 2.81 | 3.0 | `#878787` → 3.00 ✓ | `#EFF3F4` → 3.01 | 7 | DEFAULT_LINE_COMMENT, DEFAULT_BLOCK_COMMENT, DEFAULT_DOC_COMMENT | 本方案统一值 |
| `#9E880D` | `#E4ECEF` | 2.93 | 4.5 | `#776A22` → 4.53 ✓ | `#1A2428` → 4.51 | 1 | DEFAULT_METADATA | 本方案统一值；色盲变体需同步:3381FF/3381FF |
| `#A35CF0` | `#E4ECEF` | 3.28 | 4.5 | `#8945D3` → 4.55 ✓ | `#12191B` → 4.53 | 1 | TODO_DEFAULT_ATTRIBUTES | 本方案统一值 |
| `#CD4848` | `#E4ECEF` | 3.80 | 4.5 | `#C23636` → 4.53 ✓ | `#FEFEFE` → 4.51 | 1 | UNMATCHED_BRACE_ATTRIBUTES | 本方案统一值 |
| `#4E8052` | `#E4ECEF` | 3.87 | 4.5 | `#4A744D` → 4.50 ✓ | `#FCFCFC` → 4.52 | 2 | DEFAULT_DOC_COMMENT_TAG_VALUE, DEFAULT_DOC_MARKUP | 本方案统一值 |
| `#0077C2` | `#E4ECEF` | 3.97 | 4.5 | `#296E99` → 4.64 ✓ | `#F9F9F9` → 4.52 | 4 | INACTIVE_HYPERLINK_ATTRIBUTES, FOLLOWED_HYPERLINK_ATTRIBUTES, HYPERLINK_ATTRIBUTES | 本方案统一值；色盲变体需同步:3777E6/3777E6 |
| `#067D17` | `#E4ECEF` | 4.44 | 4.5 | `#1E6428` → 6.03 ✓ | `#E8EEF0` → 4.53 | 38 | DEFAULT_OPERATION_SIGN, DEFAULT_DOT, DEFAULT_SEMICOLON | 本方案统一值 |

### light/green-contrast.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#643CB8` | `#933232` | 1.05 | 4.5 | `#CEC1E8` → 4.53 ✓ | `#E8C3C3` → 4.52 | 1 | DELETED_TEXT_ATTRIBUTES | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#F0FFFF` | `#E0F2F1` | 1.13 | 3.0 | `#299898` → 3.00 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#AFDBB8` | `#E0F2F1` | 1.33 | 3.0 | `#479A58` → 3.01 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#FFB86C` | `#E0F2F1` | 1.47 | 4.5 | `#956028` → 4.56 ✓ | `#2B5A57` → 4.56 | 18 | CUSTOM_VALID_STRING_ESCAPE_ATTRIBUTES, GO_BUILTIN_FUNCTION, GO_STRUCT_EXPORTED_MEMBER | 本方案统一值 |
| `#7FC784` | `#E0F2F1` | 1.74 | 3.0 | `#459A4B` → 3.04 ✓ | — | 1 | <colors>ADDED_LINES_COLOR |  |
| `#643CB8` | `#282A36` | 1.95 | 4.5 | `#9E83D7` → 4.53 ✓ | `#C9CBD6` → 4.51 | 1 | HAML_TAG | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#9C411A` | `#282A36` | 2.16 | 4.5 | `#D6784F` → 4.52 ✓ | `#D4D5DE` → 4.51 | 1 | RUBY_HEREDOC_CONTENT | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#FF5555` | `#E0F2F1` | 2.71 | 4.5 | `#CB3030` → 4.53 ✓ | `#182F2E` → 4.50 | 5 | DEFAULT_INVALID_STRING_ESCAPE, WRONG_REFERENCES_ATTRIBUTES, JUPYTER_INLINE_VALUES | 本方案统一值 |
| `#8C8C8C` | `#E0F2F1` | 2.90 | 3.0 | `#898989` → 3.02 ✓ | `#EAF4F4` → 3.00 | 7 | DEFAULT_LINE_COMMENT, DEFAULT_BLOCK_COMMENT, DEFAULT_DOC_COMMENT | 本方案统一值 |
| `#9E880D` | `#E0F2F1` | 3.03 | 4.5 | `#796C23` → 4.56 ✓ | `#132524` → 4.54 | 1 | DEFAULT_METADATA | 本方案统一值；色盲变体需同步:3381FF/3381FF |
| `#A35CF0` | `#E0F2F1` | 3.39 | 4.5 | `#8C49D4` → 4.51 ✓ | `#0E1A19` → 4.53 | 1 | TODO_DEFAULT_ATTRIBUTES | 本方案统一值 |
| `#CD4848` | `#E0F2F1` | 3.93 | 4.5 | `#C73737` → 4.50 ✓ | `#FEFEFE` → 4.51 | 1 | UNMATCHED_BRACE_ATTRIBUTES | 本方案统一值 |
| `#4E8052` | `#E0F2F1` | 4.00 | 4.5 | `#4B764E` → 4.53 ✓ | `#FCFCFC` → 4.52 | 2 | DEFAULT_DOC_COMMENT_TAG_VALUE, DEFAULT_DOC_MARKUP | 本方案统一值 |
| `#0077C2` | `#E0F2F1` | 4.11 | 4.5 | `#296E99` → 4.79 ✓ | `#F5FAFA` → 4.51 | 4 | INACTIVE_HYPERLINK_ATTRIBUTES, FOLLOWED_HYPERLINK_ATTRIBUTES, HYPERLINK_ATTRIBUTES | 本方案统一值；色盲变体需同步:3777E6/3777E6 |

### light/green.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#643CB8` | `#933232` | 1.05 | 4.5 | `#CEC1E8` → 4.53 ✓ | `#E8C3C3` → 4.52 | 1 | DELETED_TEXT_ATTRIBUTES | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#F0FFFF` | `#E0F2F1` | 1.13 | 3.0 | `#299898` → 3.00 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#AFDBB8` | `#E0F2F1` | 1.33 | 3.0 | `#479A58` → 3.01 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#FFB86C` | `#E0F2F1` | 1.47 | 4.5 | `#956028` → 4.56 ✓ | `#2B5A57` → 4.56 | 18 | CUSTOM_VALID_STRING_ESCAPE_ATTRIBUTES, GO_BUILTIN_FUNCTION, GO_STRUCT_EXPORTED_MEMBER | 本方案统一值 |
| `#7FC784` | `#E0F2F1` | 1.74 | 3.0 | `#459A4B` → 3.04 ✓ | — | 1 | <colors>ADDED_LINES_COLOR |  |
| `#643CB8` | `#282A36` | 1.95 | 4.5 | `#9E83D7` → 4.53 ✓ | `#C9CBD6` → 4.51 | 1 | HAML_TAG | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#9C411A` | `#282A36` | 2.16 | 4.5 | `#D6784F` → 4.52 ✓ | `#D4D5DE` → 4.51 | 1 | RUBY_HEREDOC_CONTENT | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#FF5555` | `#E0F2F1` | 2.71 | 4.5 | `#CB3030` → 4.53 ✓ | `#182F2E` → 4.50 | 5 | DEFAULT_INVALID_STRING_ESCAPE, WRONG_REFERENCES_ATTRIBUTES, JUPYTER_INLINE_VALUES | 本方案统一值 |
| `#8C8C8C` | `#E0F2F1` | 2.90 | 3.0 | `#898989` → 3.02 ✓ | `#EAF4F4` → 3.00 | 7 | DEFAULT_LINE_COMMENT, DEFAULT_BLOCK_COMMENT, DEFAULT_DOC_COMMENT | 本方案统一值 |
| `#9E880D` | `#E0F2F1` | 3.03 | 4.5 | `#796C23` → 4.56 ✓ | `#132524` → 4.54 | 1 | DEFAULT_METADATA | 本方案统一值；色盲变体需同步:3381FF/3381FF |
| `#A35CF0` | `#E0F2F1` | 3.39 | 4.5 | `#8C49D4` → 4.51 ✓ | `#0E1A19` → 4.53 | 1 | TODO_DEFAULT_ATTRIBUTES | 本方案统一值 |
| `#CD4848` | `#E0F2F1` | 3.93 | 4.5 | `#C73737` → 4.50 ✓ | `#FEFEFE` → 4.51 | 1 | UNMATCHED_BRACE_ATTRIBUTES | 本方案统一值 |
| `#4E8052` | `#E0F2F1` | 4.00 | 4.5 | `#4B764E` → 4.53 ✓ | `#FCFCFC` → 4.52 | 2 | DEFAULT_DOC_COMMENT_TAG_VALUE, DEFAULT_DOC_MARKUP | 本方案统一值 |
| `#0077C2` | `#E0F2F1` | 4.11 | 4.5 | `#296E99` → 4.79 ✓ | `#F5FAFA` → 4.51 | 4 | INACTIVE_HYPERLINK_ATTRIBUTES, FOLLOWED_HYPERLINK_ATTRIBUTES, HYPERLINK_ATTRIBUTES | 本方案统一值；色盲变体需同步:3777E6/3777E6 |

### light/grey.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#643CB8` | `#933232` | 1.05 | 4.5 | `#CEC1E8` → 4.53 ✓ | `#E8C3C3` → 4.52 | 1 | DELETED_TEXT_ATTRIBUTES | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#DFE1E5` | `#F0F4F4` | 1.18 | 3.0 | `#858D9C` → 3.01 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#AFDBB8` | `#F0F4F4` | 1.39 | 3.0 | `#489D5A` → 3.03 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#FFB86C` | `#F0F4F4` | 1.54 | 4.5 | `#9A6329` → 4.52 ✓ | `#425757` → 4.51 | 18 | CUSTOM_VALID_STRING_ESCAPE_ATTRIBUTES, GO_BUILTIN_FUNCTION, GO_STRUCT_EXPORTED_MEMBER | 本方案统一值 |
| `#7FC784` | `#F0F4F4` | 1.82 | 3.0 | `#479E4D` → 3.02 ✓ | — | 1 | <colors>ADDED_LINES_COLOR |  |
| `#643CB8` | `#282A36` | 1.95 | 4.5 | `#9E83D7` → 4.53 ✓ | `#C9CBD6` → 4.51 | 1 | HAML_TAG | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#9C411A` | `#282A36` | 2.16 | 4.5 | `#D6784F` → 4.52 ✓ | `#D4D5DE` → 4.51 | 1 | RUBY_HEREDOC_CONTENT | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#FF5555` | `#F0F4F4` | 2.84 | 4.5 | `#CF3434` → 4.52 ✓ | `#232D2D` → 4.50 | 5 | DEFAULT_INVALID_STRING_ESCAPE, WRONG_REFERENCES_ATTRIBUTES, JUPYTER_INLINE_VALUES | 本方案统一值 |
| `#9E880D` | `#F0F4F4` | 3.17 | 4.5 | `#7D6F24` → 4.54 ✓ | `#1C2424` → 4.51 | 1 | DEFAULT_METADATA | 本方案统一值；色盲变体需同步:3381FF/3381FF |
| `#A35CF0` | `#F0F4F4` | 3.54 | 4.5 | `#8F4DD5` → 4.52 ✓ | `#141919` → 4.52 | 1 | TODO_DEFAULT_ATTRIBUTES | 本方案统一值 |
| `#CD4848` | `#F0F4F4` | 4.11 | 4.5 | `#C93B3B` → 4.54 ✓ | `#FEFEFE` → 4.51 | 1 | UNMATCHED_BRACE_ATTRIBUTES | 本方案统一值 |
| `#4E8052` | `#F0F4F4` | 4.18 | 4.5 | `#4C7950` → 4.56 ✓ | `#FCFCFC` → 4.52 | 2 | DEFAULT_DOC_COMMENT_TAG_VALUE, DEFAULT_DOC_MARKUP | 本方案统一值 |
| `#0077C2` | `#F0F4F4` | 4.29 | 4.5 | `#296E99` → 5.01 ✓ | `#F9F9F9` → 4.52 | 4 | INACTIVE_HYPERLINK_ATTRIBUTES, FOLLOWED_HYPERLINK_ATTRIBUTES, HYPERLINK_ATTRIBUTES | 本方案统一值；色盲变体需同步:3777E6/3777E6 |

### light/pink.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#643CB8` | `#933232` | 1.05 | 4.5 | `#CEC1E8` → 4.53 ✓ | `#E8C3C3` → 4.52 | 1 | DELETED_TEXT_ATTRIBUTES | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#FFE6E7` | `#FDF5F6` | 1.10 | 3.0 | `#DD6D72` → 3.01 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#E7CFDF` | `#FDF5F6` | 1.36 | 3.0 | `#BD7AA7` → 3.01 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#FFB86C` | `#FDF5F6` | 1.59 | 4.5 | `#9D652A` → 4.52 ✓ | `#992937` → 4.52 | 18 | CUSTOM_VALID_STRING_ESCAPE_ATTRIBUTES, GO_BUILTIN_FUNCTION, GO_STRUCT_EXPORTED_MEMBER | 本方案统一值 |
| `#7FC784` | `#FDF5F6` | 1.88 | 3.0 | `#48A14E` → 3.02 ✓ | — | 1 | <colors>ADDED_LINES_COLOR |  |
| `#643CB8` | `#282A36` | 1.95 | 4.5 | `#9E83D7` → 4.53 ✓ | `#C9CBD6` → 4.51 | 1 | HAML_TAG | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#9C411A` | `#282A36` | 2.16 | 4.5 | `#D6784F` → 4.52 ✓ | `#D4D5DE` → 4.51 | 1 | RUBY_HEREDOC_CONTENT | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#FF5555` | `#FDF5F6` | 2.93 | 4.5 | `#D03939` → 4.52 ✓ | `#4E1920` → 4.50 | 5 | DEFAULT_INVALID_STRING_ESCAPE, WRONG_REFERENCES_ATTRIBUTES, JUPYTER_INLINE_VALUES | 本方案统一值 |
| `#9E880D` | `#FDF5F6` | 3.27 | 4.5 | `#7F7124` → 4.56 ✓ | `#3E151A` → 4.51 | 1 | DEFAULT_METADATA | 本方案统一值；色盲变体需同步:3381FF/3381FF |
| `#A35CF0` | `#FDF5F6` | 3.66 | 4.5 | `#9150D6` → 4.53 ✓ | `#2C0F13` → 4.51 | 1 | TODO_DEFAULT_ATTRIBUTES | 本方案统一值 |
| `#4E8052` | `#FDF5F6` | 4.32 | 4.5 | `#4E7C51` → 4.52 ✓ | `#FCFCFC` → 4.52 | 2 | DEFAULT_DOC_COMMENT_TAG_VALUE, DEFAULT_DOC_MARKUP | 本方案统一值 |
| `#0077C2` | `#FDF5F6` | 4.43 | 4.5 | `#296E99` → 5.17 ✓ | `#F9F9F9` → 4.52 | 4 | INACTIVE_HYPERLINK_ATTRIBUTES, FOLLOWED_HYPERLINK_ATTRIBUTES, HYPERLINK_ATTRIBUTES | 本方案统一值；色盲变体需同步:3777E6/3777E6 |

### light/yellow.xml

| 色值 | 背景 | 原对比度 | 目标 | 建议值 ★ | 改背景建议（备选） | 影响属性数 | 示例属性 | 备注 |
|---|---|---|---|---|---|---|---|---|
| `#643CB8` | `#933232` | 1.05 | 4.5 | `#CEC1E8` → 4.53 ✓ | `#E8C3C3` → 4.52 | 1 | DELETED_TEXT_ATTRIBUTES | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#FFF6CF` | `#FFFBE8` | 1.05 | 3.0 | `#A7902B` → 3.03 ✓ | — | 1 | <colors>CARET_ROW_COLOR |  |
| `#F5D7E5` | `#FFFBE8` | 1.28 | 3.0 | `#DB6DA0` → 3.01 ✓ | — | 1 | <colors>SELECTION_BACKGROUND |  |
| `#FFB86C` | `#FFFBE8` | 1.64 | 4.5 | `#A1672A` → 4.50 ✓ | `#5E521D` → 4.56 | 18 | CUSTOM_VALID_STRING_ESCAPE_ATTRIBUTES, GO_BUILTIN_FUNCTION, GO_STRUCT_EXPORTED_MEMBER | 本方案统一值 |
| `#7FC784` | `#FFFBE8` | 1.94 | 3.0 | `#49A44F` → 3.01 ✓ | — | 1 | <colors>ADDED_LINES_COLOR |  |
| `#643CB8` | `#282A36` | 1.95 | 4.5 | `#9E83D7` → 4.53 ✓ | `#C9CBD6` → 4.51 | 1 | HAML_TAG | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#9C411A` | `#282A36` | 2.16 | 4.5 | `#D6784F` → 4.52 ✓ | `#D4D5DE` → 4.51 | 1 | RUBY_HEREDOC_CONTENT | 自带背景·单独定值；本方案内无统一值·按使用点单独定值 |
| `#B7A974` | `#FFFBE8` | 2.26 | 4.5 | `#7F7345` → 4.55 ✓ | `#463E17` → 4.56 | 3 | DEFAULT_BRACES, DEFAULT_PARENTHS, DEFAULT_BRACKETS | 本方案统一值 |
| `#FF5555` | `#FFFBE8` | 3.03 | 4.5 | `#D13D3D` → 4.55 ✓ | `#302B11` → 4.52 | 5 | DEFAULT_INVALID_STRING_ESCAPE, WRONG_REFERENCES_ATTRIBUTES, JUPYTER_INLINE_VALUES | 本方案统一值 |
| `#9E880D` | `#FFFBE8` | 3.38 | 4.5 | `#827425` → 4.52 ✓ | `#27220E` → 4.53 | 1 | DEFAULT_METADATA | 本方案统一值；色盲变体需同步:9e880d/9e880d |
| `#A35CF0` | `#FFFBE8` | 3.78 | 4.5 | `#9354D7` → 4.50 ✓ | `#1B180A` → 4.52 | 1 | TODO_DEFAULT_ATTRIBUTES | 本方案统一值 |
| `#4E8052` | `#FFFBE8` | 4.46 | 4.5 | `#4F7E53` → 4.55 ✓ | `#FCFCFC` → 4.52 | 2 | DEFAULT_DOC_COMMENT_TAG_VALUE, DEFAULT_DOC_MARKUP | 本方案统一值 |

## 五、共用调色板的统一建议值

8 个浅色方案共用同一套前景调色板、6 个深色方案同理。若希望**跨文件保持视觉一致**，应采用下表的统一值 —— 它同时满足该族内**全部**底色（浅色族最不利底色为 `bean-green` 的 `#C7EDCC`，深色族最不利底色为 `dark-darcula` 的 `#3C3F41`）。

> **ANSI 语义色例外**：`CONSOLE_*_OUTPUT` 是终端 ANSI 16 色的固定映射，改色会破坏用户对「红=错误、绿=成功」这类约定的识别，因此标为「保持原值·不建议修改」。若确实要提升控制台可读性，建议改 `<colors>` 的 `CONSOLE_BACKGROUND_KEY`（控制台底色）而非改 ANSI 前景色。仅当某色值下**全部**属性都是 `CONSOLE_*` 时才豁免；混用（部分语法属性 + 部分控制台属性）仍给出建议值，但标注 `ANSI 混用` 以提示连带影响。

> `4.5 约束下最低` = 统一建议值在**正文类约束**（目标 4.5）上的最低对比度；`3.0 约束下最低` = 在**次要类约束**（目标 3.0，如注释）上的最低对比度 —— 同一色值可能同时落在两类约束上，故分列两栏，避免「最低对比度」被误读为未达标。

### 浅色族（8 个方案共用）

涉及文件：`light/bean-green.xml`、`light/buff.xml`、`light/celadon.xml`、`light/green-contrast.xml`、`light/green.xml`、`light/grey.xml`、`light/pink.xml`、`light/yellow.xml`

| 色值 | 原对比度（族内最差） | 统一建议值 | 4.5 约束下最低 | 3.0 约束下最低 | 涉及底色数 | 状态 |
|---|---|---|---|---|---|---|
| `#643CB8` | 1.05 | `#9E83D7` | 2.44 | 2.46 | 9 | ✗ 无统一值，需逐文件处理 |
| `#FFB86C` | 1.33 | `#8C5B27` | 4.51 | — | 7 | ✓ 可跨文件统一替换 |
| `#9C411A` | 2.16 | `#C45C2F` | 3.34 | — | 8 | ✗ 无统一值，需逐文件处理 |
| `#B7A974` | 2.19 | `#7D7144` | 4.53 | — | 2 | ✓ 可跨文件统一替换 |
| `#FF5555` | 2.46 | `#BF2E2E` | 4.50 | — | 7 | ✓ 可跨文件统一替换 |
| `#8C8C8C` | 2.63 | `#828282` | — | 3.00 | 7 | ✓ 可跨文件统一替换 |
| `#9E880D` | 2.74 | `#726621` | 4.51 | — | 7 | ✓ 可跨文件统一替换 |
| `#A35CF0` | 3.07 | `#853FD2` | 4.51 | — | 7 | ✓ 可跨文件统一替换 |
| `#CD4848` | 3.56 | `#BA3434` | 4.52 | — | 4 | ✓ 可跨文件统一替换 |
| `#4E8052` | 3.62 | `#466F4A` | 4.52 | 4.52 | 7 | ✓ 可跨文件统一替换 |
| `#0077C2` | 3.72 | `#286B96` | 4.52 | — | 7 | ✓ 可跨文件统一替换 |
| `#067D17` | 4.15 | `#1E6428` | 5.64 | 5.64 | 7 | ✓ 可跨文件统一替换 |
| `#414D41` | 4.18 | `#3D473D` | 4.56 | — | 5 | ✓ 可跨文件统一替换 |
| `#007297` | 4.26 | `#226075` | 5.47 | — | 7 | ✓ 可跨文件统一替换 |
| `#B7447B` | 4.74 | `#B7447B` | 4.74 | 4.74 | 1 | 保持原值·已达标 |
| `#46785E` | 4.76 | `#46785E` | 4.76 | — | 2 | 保持原值·已达标 |
| `#545454` | 4.94 | `#545454` | 4.94 | — | 2 | 保持原值·已达标 |
| `#14646E` | 5.33 | `#14646E` | — | 5.33 | 7 | 保持原值·已达标 |
| `#8D4532` | 5.42 | `#8D4532` | 5.42 | — | 7 | 保持原值·已达标 |
| `#174AD4` | 5.53 | `#174AD4` | 5.53 | — | 7 | 保持原值·已达标 |
| `#5B5B5B` | 5.80 | `#5B5B5B` | 5.80 | — | 2 | 保持原值·已达标 |
| `#933232` | 5.98 | `#933232` | 5.98 | — | 7 | 保持原值·已达标 |
| `#871790` | 6.42 | `#871790` | 6.42 | — | 7 | 保持原值·已达标 |
| `#27282E` | 6.56 | `#27282E` | 6.56 | — | 1 | 保持原值·已达标 |
| `#383A42` | 8.86 | `#383A42` | 8.86 | — | 7 | 保持原值·已达标 |

### 深色族（6 个方案共用）

涉及文件：`dark/dark-blue.xml`、`dark/dark-coffee.xml`、`dark/dark-darcula.xml`、`dark/dark-green.xml`、`dark/dark-violet.xml`、`dark/dark.xml`

| 色值 | 原对比度（族内最差） | 统一建议值 | 4.5 约束下最低 | 3.0 约束下最低 | 涉及底色数 | 状态 |
|---|---|---|---|---|---|---|
| `#21222C` | 1.01 | 保持原值 | 1.01 | — | 6 | ANSI 语义色·不建议修改 |
| `#6272A4` | 2.25 | `#9FA9C7` | 4.53 | — | 6 | ✓ 可跨文件统一替换；⚠ ANSI 混用（6/54 个属性为 CONSOLE_*），改色会连带影响控制台配色 |
| `#8D4532` | 2.25 | `#C6755F` | 4.53 | — | 1 | ✓ 可跨文件统一替换 |
| `#969696` | 2.29 | `#D3D3D3` | 4.53 | — | 8 | ✓ 可跨文件统一替换 |
| `#6F7587` | 2.31 | `#A5A9B5` | 4.52 | — | 6 | ✓ 可跨文件统一替换 |
| `#CD4848` | 2.33 | `#E29595` | 4.53 | — | 6 | ✓ 可跨文件统一替换 |
| `#9876AA` | 2.78 | `#B8A1C4` | 4.51 | 4.51 | 7 | ✓ 可跨文件统一替换；⚠ ANSI 混用（24/555 个属性为 CONSOLE_*），改色会连带影响控制台配色 |
| `#1D9A62` | 2.96 | `#2FC07F` | 4.53 | 4.53 | 5 | ✓ 可跨文件统一替换；⚠ ANSI 混用（5/130 个属性为 CONSOLE_*），改色会连带影响控制台配色 |
| `#B2CDE2` | 3.03 | `#EFF4F8` | 4.51 | — | 8 | ✓ 可跨文件统一替换；⚠ ANSI 混用（5/85 个属性为 CONSOLE_*），改色会连带影响控制台配色 |
| `#15845D` | 3.05 | `#2BA479` | 4.54 | 4.96 | 2 | ✓ 可跨文件统一替换；⚠ ANSI 混用（1/24 个属性为 CONSOLE_*），改色会连带影响控制台配色 |
| `#8092AB` | 3.34 | `#8092AB` | — | 3.34 | 5 | 保持原值·已达标 |
| `#FF5555` | 3.38 | `#E59393` | 4.51 | — | 6 | ✓ 可跨文件统一替换；⚠ ANSI 混用（12/42 个属性为 CONSOLE_*），改色会连带影响控制台配色 |
| `#BFA5A2` | 3.46 | `#D1BFBD` | 4.51 | — | 3 | ✓ 可跨文件统一替换；⚠ ANSI 混用（1/17 个属性为 CONSOLE_*），改色会连带影响控制台配色 |
| `#C58564` | 3.49 | `#D19E84` | 4.51 | — | 6 | ✓ 可跨文件统一替换 |
| `#A09A1D` | 3.60 | `#B3AD2D` | 4.51 | — | 6 | ✓ 可跨文件统一替换 |
| `#917679` | 3.76 | `#917679` | — | 3.76 | 1 | 保持原值·已达标 |
| `#85A658` | 3.84 | `#96B36F` | 4.53 | — | 6 | ✓ 可跨文件统一替换 |
| `#6AAB73` | 3.88 | `#7EB686` | 4.51 | — | 6 | ✓ 可跨文件统一替换 |
| `#FF6E6E` | 3.89 | 保持原值 | 3.89 | — | 6 | ANSI 语义色·不建议修改 |
| `#F8F8F2` | 3.93 | `#FFFFFF` | 4.19 | — | 17 | ✗ 无统一值，需逐文件处理；⚠ ANSI 混用（12/84 个属性为 CONSOLE_*），改色会连带影响控制台配色 |
| `#00B9AF` | 4.32 | `#2EBCB5` | 4.53 | — | 6 | ✓ 可跨文件统一替换 |
| `#B2AE60` | 4.61 | `#B2AE60` | 4.61 | — | 6 | 保持原值·已达标 |
| `#B3AE60` | 4.62 | `#B3AE60` | 4.62 | — | 6 | 保持原值·已达标 |
| `#A9B346` | 4.66 | `#A9B346` | 4.66 | — | 6 | 保持原值·已达标；⚠ ANSI 混用（6/24 个属性为 CONSOLE_*），改色会连带影响控制台配色 |
| `#65BCD9` | 4.92 | `#65BCD9` | 4.92 | — | 6 | 保持原值·已达标；⚠ ANSI 混用（6/432 个属性为 CONSOLE_*），改色会连带影响控制台配色 |
| `#F998CC` | 5.25 | `#F998CC` | 5.25 | — | 6 | 保持原值·已达标 |
| `#FF92DF` | 5.27 | 保持原值 | 5.27 | — | 6 | ANSI 语义色·不建议修改 |
| `#D6ACFF` | 5.66 | 保持原值 | 5.66 | — | 6 | ANSI 语义色·不建议修改 |
| `#E998FF` | 6.35 | `#E998FF` | 6.35 | — | 3 | 保持原值·已达标 |
| `#36FFAC` | 8.11 | `#36FFAC` | 8.11 | — | 6 | 保持原值·已达标 |
| `#69FF94` | 8.25 | 保持原值 | 8.25 | — | 6 | ANSI 语义色·不建议修改 |
| `#A4FFFF` | 9.26 | 保持原值 | 9.26 | — | 6 | ANSI 语义色·不建议修改 |
| `#FFFFA5` | 10.15 | 保持原值 | 10.15 | — | 6 | ANSI 语义色·不建议修改 |

## 六、色盲变体色不达标项

部分语法属性定义了 `deuteranopia` / `protanopia` 变体色（红绿色盲模拟）。变体色同样是实际渲染的颜色，需一并满足对比度要求。

| 文件 | 变体类型 | 色值 | 背景 | 原对比度 | 目标 | 建议值 | 状态 | 示例属性 |
|---|---|---|---|---|---|---|---|---|
| dark/dark-blue.xml | 绿色盲 | `#94907F` | `#233348` | 4.00 | 4.5 | `#9D9A8A` → 4.53 | ✓ | DEFAULT_STRING |
| dark/dark-blue.xml | 红色盲 | `#94907F` | `#233348` | 4.00 | 4.5 | `#9D9A8A` → 4.53 | ✓ | DEFAULT_STRING |
| dark/dark-darcula.xml | 绿色盲 | `#94907F` | `#3C3F41` | 3.31 | 4.5 | `#ACA99C` → 4.50 | ✓ | DEFAULT_STRING |
| dark/dark-darcula.xml | 红色盲 | `#94907F` | `#3C3F41` | 3.31 | 4.5 | `#ACA99C` → 4.50 | ✓ | DEFAULT_STRING |
| dark/dark-darcula.xml | 绿色盲 | `#66A6FF` | `#3C3F41` | 4.28 | 4.5 | `#85ACE2` → 4.54 | ✓ | DEFAULT_METADATA |
| dark/dark-darcula.xml | 红色盲 | `#66A6FF` | `#3C3F41` | 4.28 | 4.5 | `#85ACE2` → 4.54 | ✓ | DEFAULT_METADATA |
| dark/dark-green.xml | 绿色盲 | `#94907F` | `#002F38` | 4.48 | 4.5 | `#959180` → 4.54 | ✓ | DEFAULT_STRING |
| dark/dark-green.xml | 红色盲 | `#94907F` | `#002F38` | 4.48 | 4.5 | `#959180` → 4.54 | ✓ | DEFAULT_STRING |
| dark/dark.xml | 绿色盲 | `#94907F` | `#282A36` | 4.45 | 4.5 | `#959180` → 4.50 | ✓ | DEFAULT_STRING |
| dark/dark.xml | 红色盲 | `#94907F` | `#282A36` | 4.45 | 4.5 | `#959180` → 4.50 | ✓ | DEFAULT_STRING |
| light/bean-green.xml | 绿色盲 | `#3381FF` | `#C7EDCC` | 2.87 | 4.5 | `#2E63B9` → 4.56 | ✓ | DEFAULT_METADATA |
| light/bean-green.xml | 红色盲 | `#3381FF` | `#C7EDCC` | 2.87 | 4.5 | `#2E63B9` → 4.56 | ✓ | DEFAULT_METADATA |
| light/bean-green.xml | 绿色盲 | `#3777E6` | `#C7EDCC` | 3.32 | 4.5 | `#2E63BE` → 4.50 | ✓ | FOLLOWED_HYPERLINK_ATTRIBUTES |
| light/bean-green.xml | 红色盲 | `#3777E6` | `#C7EDCC` | 3.32 | 4.5 | `#2E63BE` → 4.50 | ✓ | FOLLOWED_HYPERLINK_ATTRIBUTES |
| light/bean-green.xml | 绿色盲 | `#4D4D4D` | `#96BB9B` | 3.98 | 4.5 | `#454545` → 4.51 | ✓ | FOLDED_TEXT_ATTRIBUTES |
| light/bean-green.xml | 红色盲 | `#4D4D4D` | `#96BB9B` | 3.98 | 4.5 | `#454545` → 4.51 | ✓ | FOLDED_TEXT_ATTRIBUTES |
| light/buff.xml | 绿色盲 | `#9E880D` | `#F7F7F2` | 3.27 | 4.5 | `#7F7124` → 4.56 | ✓ | DEFAULT_METADATA |
| light/buff.xml | 红色盲 | `#9E880D` | `#F7F7F2` | 3.27 | 4.5 | `#7F7124` → 4.56 | ✓ | DEFAULT_METADATA |
| light/buff.xml | 绿色盲 | `#3777E6` | `#F7F7F2` | 3.95 | 4.5 | `#366ED0` → 4.55 | ✓ | HYPERLINK_ATTRIBUTES |
| light/buff.xml | 红色盲 | `#3777E6` | `#F7F7F2` | 3.95 | 4.5 | `#366ED0` → 4.55 | ✓ | HYPERLINK_ATTRIBUTES |
| light/celadon.xml | 绿色盲 | `#3381FF` | `#E4ECEF` | 3.06 | 4.5 | `#2F68C4` → 4.50 | ✓ | DEFAULT_METADATA |
| light/celadon.xml | 红色盲 | `#3381FF` | `#E4ECEF` | 3.06 | 4.5 | `#2F68C4` → 4.50 | ✓ | DEFAULT_METADATA |
| light/celadon.xml | 绿色盲 | `#3777E6` | `#E4ECEF` | 3.55 | 4.5 | `#3067C7` → 4.51 | ✓ | FOLLOWED_HYPERLINK_ATTRIBUTES |
| light/celadon.xml | 红色盲 | `#3777E6` | `#E4ECEF` | 3.55 | 4.5 | `#3067C7` → 4.51 | ✓ | FOLLOWED_HYPERLINK_ATTRIBUTES |
| light/green-contrast.xml | 绿色盲 | `#3381FF` | `#E0F2F1` | 3.17 | 4.5 | `#306AC9` → 4.50 | ✓ | DEFAULT_METADATA |
| light/green-contrast.xml | 红色盲 | `#3381FF` | `#E0F2F1` | 3.17 | 4.5 | `#306AC9` → 4.50 | ✓ | DEFAULT_METADATA |
| light/green-contrast.xml | 绿色盲 | `#3777E6` | `#E0F2F1` | 3.67 | 4.5 | `#3069CC` → 4.51 | ✓ | FOLLOWED_HYPERLINK_ATTRIBUTES |
| light/green-contrast.xml | 红色盲 | `#3777E6` | `#E0F2F1` | 3.67 | 4.5 | `#3069CC` → 4.51 | ✓ | FOLLOWED_HYPERLINK_ATTRIBUTES |
| light/green.xml | 绿色盲 | `#3381FF` | `#E0F2F1` | 3.17 | 4.5 | `#306AC9` → 4.50 | ✓ | DEFAULT_METADATA |
| light/green.xml | 红色盲 | `#3381FF` | `#E0F2F1` | 3.17 | 4.5 | `#306AC9` → 4.50 | ✓ | DEFAULT_METADATA |
| light/green.xml | 绿色盲 | `#3777E6` | `#E0F2F1` | 3.67 | 4.5 | `#3069CC` → 4.51 | ✓ | FOLLOWED_HYPERLINK_ATTRIBUTES |
| light/green.xml | 红色盲 | `#3777E6` | `#E0F2F1` | 3.67 | 4.5 | `#3069CC` → 4.51 | ✓ | FOLLOWED_HYPERLINK_ATTRIBUTES |
| light/grey.xml | 绿色盲 | `#3381FF` | `#F0F4F4` | 3.31 | 4.5 | `#306CCD` → 4.56 | ✓ | DEFAULT_METADATA |
| light/grey.xml | 红色盲 | `#3381FF` | `#F0F4F4` | 3.31 | 4.5 | `#306CCD` → 4.56 | ✓ | DEFAULT_METADATA |
| light/grey.xml | 绿色盲 | `#3777E6` | `#F0F4F4` | 3.83 | 4.5 | `#336CCF` → 4.53 | ✓ | FOLLOWED_HYPERLINK_ATTRIBUTES |
| light/grey.xml | 红色盲 | `#3777E6` | `#F0F4F4` | 3.83 | 4.5 | `#336CCF` → 4.53 | ✓ | FOLLOWED_HYPERLINK_ATTRIBUTES |
| light/pink.xml | 绿色盲 | `#3381FF` | `#FDF5F6` | 3.42 | 4.5 | `#346FCF` → 4.53 | ✓ | DEFAULT_METADATA |
| light/pink.xml | 红色盲 | `#3381FF` | `#FDF5F6` | 3.42 | 4.5 | `#346FCF` → 4.53 | ✓ | DEFAULT_METADATA |
| light/pink.xml | 绿色盲 | `#3777E6` | `#FDF5F6` | 3.96 | 4.5 | `#376FD0` → 4.50 | ✓ | HYPERLINK_ATTRIBUTES |
| light/pink.xml | 红色盲 | `#3777E6` | `#FDF5F6` | 3.96 | 4.5 | `#376FD0` → 4.50 | ✓ | HYPERLINK_ATTRIBUTES |
| light/yellow.xml | 绿色盲 | `#9E880D` | `#FFFBE8` | 3.38 | 4.5 | `#827425` → 4.52 | ✓ | DEFAULT_METADATA |
| light/yellow.xml | 红色盲 | `#9E880D` | `#FFFBE8` | 3.38 | 4.5 | `#827425` → 4.52 | ✓ | DEFAULT_METADATA |
| light/yellow.xml | 绿色盲 | `#3777E6` | `#FFFBE8` | 4.09 | 4.5 | `#3A71D1` → 4.53 | ✓ | HYPERLINK_ATTRIBUTES |
| light/yellow.xml | 红色盲 | `#3777E6` | `#FFFBE8` | 4.09 | 4.5 | `#3A71D1` → 4.53 | ✓ | HYPERLINK_ATTRIBUTES |

## 七、核算方法

**相对亮度**（WCAG 2.1 定义）：

```
R' = R/255;  c = R' <= 0.04045 ? R'/12.92 : ((R'+0.055)/1.055)^2.4
L  = 0.2126*R' + 0.7152*G' + 0.0722*B'
对比度 = (L_lighter + 0.05) / (L_darker + 0.05)
```

**建议色值生成**：在 HSL 空间保持 Hue 不变，沿 L 方向扫描，取满足全部约束的**最小**偏移；Saturation 按 `min(S, 0.62)` 封顶，并随明度偏移做衰减（变暗时 ×(0.72+0.28·L/0.5)，变亮时 ×(0.78+0.22·(1−L)/0.28)）。

**判定范围**：

- theme.json：`colors` 块中的语义色（error/warning/info/success 等）与强调色（accentColor/underlineColor/iconBackground）、`primaryForeground` 对各底色（main/menu/primary/selection/selectionInactive/secondaryBackground）、`ui` 树内所有同节点的 `xxxForeground` ↔ `xxxBackground` 配对；
- XML：`<attributes>` 段全部语法属性的 `FOREGROUND`（含属性自带 `BACKGROUND` 的情形）、`<colors>` 段的 `SELECTION_BACKGROUND` / `CARET_ROW_COLOR` / `ADDED_LINES_COLOR` / `DELETED_LINES_COLOR` / `ERROR_STRIPE_COLOR`，以及全部 `deuteranopia` / `protanopia` 变体色。

**未纳入判定的项**：`<colors>` 段中的纯装饰色（`SCROLLBAR_*`、`INDENT_GUIDE`、`TEARLINE_COLOR`、`VCS_ANNOTATIONS_COLOR_*` 等）—— 它们不是承载文本的表面，WCAG 1.4.3 不适用；亦未对深色/浅色主题之间的跨主题组合做判定（同一时刻只会启用一套主题）。

