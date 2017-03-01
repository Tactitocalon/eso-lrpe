--Register LAM with LibStub
local MAJOR, MINOR = "LibNotifications", 3
local libNotification, oldminor = LibStub:NewLibrary(MAJOR, MINOR)
if not libNotification then return end --the same or newer version of this lib is already loaded into memory

local KEYBOARD_NOTIFICATION_ICONS = { -- copied from notifications_keyboard.lua
    [NOTIFICATION_TYPE_FRIEND] = "EsoUI/Art/Notifications/notificationIcon_friend.dds",
    [NOTIFICATION_TYPE_GUILD] = "EsoUI/Art/Notifications/notificationIcon_guild.dds",
    [NOTIFICATION_TYPE_GUILD_MOTD] = "EsoUI/Art/Notifications/notificationIcon_guild.dds",
    [NOTIFICATION_TYPE_CAMPAIGN_QUEUE] = "EsoUI/Art/Notifications/notificationIcon_campaignQueue.dds",
    [NOTIFICATION_TYPE_RESURRECT] = "EsoUI/Art/Notifications/notificationIcon_resurrect.dds",
    [NOTIFICATION_TYPE_GROUP] = "EsoUI/Art/Notifications/notificationIcon_group.dds",
    [NOTIFICATION_TYPE_TRADE] = "EsoUI/Art/Notifications/notificationIcon_trade.dds",
    [NOTIFICATION_TYPE_QUEST_SHARE] = "EsoUI/Art/Notifications/notificationIcon_quest.dds",
    [NOTIFICATION_TYPE_PLEDGE_OF_MARA] = "EsoUI/Art/Notifications/notificationIcon_mara.dds",
    [NOTIFICATION_TYPE_CUSTOMER_SERVICE] = "EsoUI/Art/Notifications/notification_cs.dds",
    [NOTIFICATION_TYPE_LEADERBOARD] = "EsoUI/Art/Notifications/notificationIcon_leaderboard.dds",
    [NOTIFICATION_TYPE_COLLECTIONS] = "EsoUI/Art/Notifications/notificationIcon_collections.dds",
    [NOTIFICATION_TYPE_LFG] = "EsoUI/Art/Notifications/notificationIcon_group.dds",
    [NOTIFICATION_TYPE_POINTS_RESET] = "EsoUI/Art/MenuBar/Gamepad/gp_playerMenu_icon_character.dds",
    [NOTIFICATION_TYPE_CRAFT_BAG_AUTO_TRANSFER] = "EsoUI/Art/Notifications/notificationIcon_autoTransfer.dds",
    [NOTIFICATION_TYPE_GROUP_ELECTION] = "EsoUI/Art/Notifications/notificationIcon_autoTransfer.dds",
}

local GAMEPAD_NOTIFICATION_ICONS = { -- copied from notifications_gamepad.lua
    [NOTIFICATION_TYPE_FRIEND] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_friend.dds",
    [NOTIFICATION_TYPE_GUILD] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_guild.dds",
    [NOTIFICATION_TYPE_GUILD_MOTD] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_guild.dds",
    [NOTIFICATION_TYPE_CAMPAIGN_QUEUE] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_campaignQueue.dds",
    [NOTIFICATION_TYPE_RESURRECT] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_resurrect.dds",
    [NOTIFICATION_TYPE_GROUP] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_group.dds",
    [NOTIFICATION_TYPE_TRADE] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_trade.dds",
    [NOTIFICATION_TYPE_QUEST_SHARE] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_quest.dds",
    [NOTIFICATION_TYPE_PLEDGE_OF_MARA] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_mara.dds",
    [NOTIFICATION_TYPE_CUSTOMER_SERVICE] = "EsoUI/Art/Notifications/Gamepad/gp_notification_cs.dds",
    [NOTIFICATION_TYPE_LEADERBOARD] = "EsoUI/Art/Notifications/Gamepad/gp_notification_leaderboardAccept_down.dds",
    [NOTIFICATION_TYPE_COLLECTIONS] = "EsoUI/Art/MenuBar/Gamepad/gp_playerMenu_icon_collections.dds",
    [NOTIFICATION_TYPE_LFG] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_group.dds",
    [NOTIFICATION_TYPE_POINTS_RESET] = "EsoUI/Art/MenuBar/Gamepad/gp_playerMenu_icon_character.dds",
    [NOTIFICATION_TYPE_CRAFT_BAG_AUTO_TRANSFER] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_autoTransfer.dds",
    [NOTIFICATION_TYPE_GROUP_ELECTION] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_autoTransfer.dds",
    [NOTIFICATION_TYPE_DUEL] = "EsoUI/Art/Notifications/Gamepad/gp_notificationIcon_duel.dds",
}

local DATA_TYPE_TO_TEMPLATE = { -- also copied from notifications_gamepad.lua
    [NOTIFICATIONS_REQUEST_DATA] = "ZO_GamepadNotificationsRequestRow",
    [NOTIFICATIONS_YES_NO_DATA] = "ZO_GamepadNotificationsYesNoRow",
    [NOTIFICATIONS_WAITING_DATA] = "ZO_GamepadNotificationsWaitingRow",
    [NOTIFICATIONS_LEADERBOARD_DATA] = "ZO_GamepadNotificationsLeaderboardRow",
    [NOTIFICATIONS_ALERT_DATA] = "ZO_GamepadNotificationsAlertRow",
    [NOTIFICATIONS_COLLECTIBLE_DATA] = "ZO_GamepadNotificationsCollectibleRow",
    [NOTIFICATIONS_LFG_JUMP_DUNGEON_DATA] = "ZO_GamepadNotificationsLFGJumpDungeonRow",
    [NOTIFICATIONS_LFG_READY_CHECK_DATA] = "ZO_GamepadNotificationsLFGReadyCheckRow",
    [NOTIFICATIONS_LFG_FIND_REPLACEMENT_DATA] = "ZO_GamepadNotificationsLFGFindReplacementRow",
}

--==========================================================================--
--=== OVERRIDES ===--
--==========================================================================--
-- Override so we can set our own texture & heading
function NOTIFICATIONS:SetupBaseRow(control, data)
    ZO_SortFilterList.SetupRow(self.sortFilterList, control, data)

    local notificationType = data.notificationType
    local texture          = data.texture or KEYBOARD_NOTIFICATION_ICONS[notificationType]
    local headingText      = data.heading or zo_strformat(SI_NOTIFICATIONS_TYPE_FORMATTER, GetString("SI_NOTIFICATIONTYPE", notificationType))

    control.notificationType = notificationType
    control.index            = data.index

    GetControl(control, "Icon"):SetTexture(texture)
    GetControl(control, "Type"):SetText(headingText)
end

-- Override so we can set our own texture & heading
function GAMEPAD_NOTIFICATIONS:AddDataEntry(dataType, data, isHeader)
    local texture     = data.texture or GAMEPAD_NOTIFICATION_ICONS[data.notificationType]
    local headingText = data.heading or zo_strformat(SI_GAMEPAD_NOTIFICATIONS_TYPE_FORMATTER, GetString("SI_NOTIFICATIONTYPE", data.notificationType))

    local entryData = ZO_GamepadEntryData:New(data.shortDisplayText, texture)
    entryData.data  = data
    entryData:SetIconTintOnSelection(true)
    entryData:SetIconDisabledTintOnSelection(true)

    if isHeader then
        entryData:SetHeader(headingText)
        self.list:AddEntryWithHeader(DATA_TYPE_TO_TEMPLATE[dataType], entryData)
    else
        self.list:AddEntry(DATA_TYPE_TO_TEMPLATE[dataType], entryData)
    end
end

--==========================================================================--
--=== Base Provider ===--
--==========================================================================--
local libNotificationProvider = ZO_NotificationProvider:Subclass()

function libNotificationProvider:New(notificationManager)
    local provider = ZO_NotificationProvider.New(self, notificationManager)
    table.insert(notificationManager.providers, provider)

    return provider
end

function libNotificationProvider:BuildNotificationList()
    ZO_ClearNumericallyIndexedTable(self.list)

    -- Use a copy so it wont delete/alter the addons original msg list/table.
    local notifications = self.providerLinkTable.notifications
    self.list = ZO_DeepTableCopy(notifications)
end

--==========================================================================--
--=== Keyboard Provider ===--
--==========================================================================--
local libNotificationKeyboardProvider = libNotificationProvider:Subclass()

function libNotificationKeyboardProvider:New(notificationManager)
    local keyboardProvider = libNotificationProvider.New(self, notificationManager)

    return keyboardProvider
end

function libNotificationKeyboardProvider:Accept(data)
    if data.keyboardAcceptCallback then
        data.keyboardAcceptCallback(data)
    end
end

function libNotificationKeyboardProvider:Decline(data, button, openedFromKeybind)
    if data.keybaordDeclineCallback then
        data.keybaordDeclineCallback(data)
    end
end


--==========================================================================--
--=== Gamepad Provider ===--
--==========================================================================--
local libNotificationGamepadProvider = libNotificationProvider:Subclass()

function libNotificationGamepadProvider:New(notificationManager)
    local gamepadProvider = libNotificationProvider.New(self, notificationManager)

    return gamepadProvider
end

function libNotificationGamepadProvider:Accept(data)
    if data.gamepadAcceptCallback then
        data.gamepadAcceptCallback(data)
    end
end

function libNotificationGamepadProvider:Decline(data, button, openedFromKeybind)
    if data.gamepadDeclineCallback then
        data.gamepadDeclineCallback(data)
    end
end

--=============================================================--
--=== LIBRARY FUNCTIONS ===--
--=============================================================--
function libNotification:CreateProvider()
    local keyboardProvider = libNotificationKeyboardProvider:New(NOTIFICATIONS)
    local gamepadProvider  = libNotificationGamepadProvider:New(GAMEPAD_NOTIFICATIONS)

    local provider = {
        notifications       = {},
        keyboardProvider    = keyboardProvider,
        gamepadProvider     = gamepadProvider,
        UpdateNotifications = function()
            keyboardProvider:pushUpdateCallback()
            gamepadProvider:pushUpdateCallback()
        end,
    }
    keyboardProvider.providerLinkTable = provider
    gamepadProvider.providerLinkTable  = provider

    return provider
end
