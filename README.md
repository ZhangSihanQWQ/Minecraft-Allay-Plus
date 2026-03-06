# AllayPlus

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen.svg)](https://www.minecraft.net/)
[![Platform](https://img.shields.io/badge/Platform-Fabric-blue.svg)](https://fabricmc.net/)

**English** | [中文](README_zh.md)

**AllayPlus** is a Fabric mod designed to enhance the vanilla Allay. It introduces smarter and broader item collection logic, optimizes server performance by refactoring underlying AI mechanisms, and provides a customizable rule system.

---

## Features

### 1. Fuzzy Item Classification
Breaks the strict vanilla definition of "identical items," allowing Allays to collect items based on broader categories:

* **Potions**: Ignores effects (e.g., Strength, Regeneration) and duration, categorizing only by container type:
  * Potion
  * Splash Potion
  * Lingering Potion
* **Music Discs**: Allays now love all music; all types of music discs are treated as a single category.
* **Banner Patterns**: Ignores specific pattern types; any banner pattern will be collected.
* **Boats & Boats with Chests**: Ignores wood types (e.g., Oak, Bamboo). Only distinguishes between:
  * Boat
  * Boat with Chest
* **Horse Armor**: All tiers (Iron, Golden, Diamond) are now grouped together.
* **Beds**: All colors of beds are now treated as the same category.
* **Pottery Sherds**: Different patterns of sherds are now considered identical.
* **Smithing Templates**: All templates are grouped together, including Netherite Upgrade and all Armor Trims.

### 2. Performance Optimization
To address MSPT spikes caused by large groups of Allays, the mod implements a streamlined optimization strategy:

* **Low-Frequency Polling**: Logic checks run every **20 ticks** (1 second) to minimize overhead.
* **Dynamic Memory Masking**: If a player is not in the Allay's line of sight, the system temporarily masks the `LIKED_PLAYER` memory. If no `LIKED_NOTEBLOCK` task exists, the Allay enters a "Free Wander" state. This prevents massive pathfinding calculations caused by Allays attempting to follow players in confined spaces.

### 3. Customizable Rule System
Multiple configurable rules have been added. Modifying these rules requires **Permission Level 2 (OP)**.

| Rule Name | Command Usage | Description | Default |
| :--- | :--- | :--- | :--- |
| **Silent Resonance** | `/allayplus silentResonanceEnabled <true\|false>` | When enabled, Note Blocks blocked by a block above ("Silent") will still trigger the Allay's response logic via Game Events. | `false` |
| **Throw Cooldown** | `/allayplus throwCooldownTime <ticks>` | Customizes the cooldown before an Allay can pick up items again after throwing. Set to `0` for no cooldown, `-1` for vanilla (60 ticks). | `-1` |

> **Note**: Regarding `throwCooldownTime`, it is not recommended to set it too low (e.g., 0-5 ticks). Otherwise, the Allay may immediately pick up the item it just threw before it can be collected by a Hopper or Note Block.

---

## Requirements

* **Game Version**: `1.21.1`
* **Loader**: `Fabric Loader >= 0.16.14`
* **Dependencies**: `Fabric API`
