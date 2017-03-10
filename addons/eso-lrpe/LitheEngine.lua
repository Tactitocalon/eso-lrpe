LitheGUI = LitheGUI or {}
LitheEngine = LitheEngine or {}
LitheDatabase = LitheDatabase or {}

-- Addon constants
local ADDON_NAME = "eso-lrpe"
local ADDON_VERSION = 2

local SECONDS_UNTIL_DATABASE_UPDATE_NAG = 60 * 60 * 48

-- Library registration
local LIB_CHAT = LibStub('libChat-1.0')
local LIB_NOTIFICATIONS = LibStub:GetLibrary("LibNotifications")

local lastExaminedPlayer = nil

function LitheEngine:Initialize()
	local notificationsProvider = LIB_NOTIFICATIONS:CreateProvider()
	
	-- TODO: Verify intgrity of LRPE database. If database fails integrity check, disable this addon.
	
	-- Check last time we updated the LRPE database.
	if (GetTimeStamp() > (LitheDatabase.lastUpdateTimestamp + SECONDS_UNTIL_DATABASE_UPDATE_NAG)) then
		local updateDatabaseMsg = {
			dataType = NOTIFICATIONS_ALERT_DATA,
			secsSinceRequest = ZO_NormalizeSecondsSince(0),
			note = "Please run the ESO-LRPE Updater tool and then use the /reloadui command.",
			message = "Roleplay profiles database is out of date, please update!",
			heading = "ESO-LRPE",
			texture = "/esoui/art/miscellaneous/eso_icon_warning.dds",
			shortDisplayText = "ESO-LRPE",
			controlsOwnSounds = true,
			keyboardAcceptCallback = OnKeyboardAccept,
			keybaordDeclineCallback = OnKeyboardDecline,
			gamepadAcceptCallback = OnGamepadAccept,
			gamepadDeclineCallback = OnGamepadDecline,
			data = {}
		}
		
		table.insert(notificationsProvider.notifications, updateDatabaseMsg)
		notificationsProvider:UpdateNotifications()
	end

	SLASH_COMMANDS["/examine"] = LitheEngine.OnCommandExamine
	SLASH_COMMANDS["/ex"] = LitheEngine.OnCommandExamine
	ZO_CreateStringId("SI_BINDING_NAME_ESO_LRPE_EXAMINE_MOUSEOVER", "Examine my target")
	LitheEngine:InitializeNameRewrite()
end

function LitheEngine.OnCommandExamine(commandData)
	-- Extract the name that we are examining
	local targetName = commandData or ""
	-- Trim string
	targetName = string.gsub(targetName , "%s$", "")

	LitheEngine.Examine(targetName)
end

function LRPE_ExamineMouseover()
	local profileView = LitheGUI:GetProfileView()
	
	if DoesUnitExist('reticleover') and IsUnitPlayer('reticleover') then
		local targetName = GetUnitName('reticleover') or ""
		-- If the profile view is still open, and we're not examining a different player, we want to close it.
		if (lastExaminedPlayer == targetName and profileView:IsVisible()) then
			lastExaminedPlayer = ""
			profileView:SetVisible(false)
		else	
			LitheEngine.Examine(targetName)
		end	
	else
		profileView:SetVisible(false)
	end
end

function LitheEngine.Examine(targetName)
	lastExaminedPlayer = targetName
	if (targetName == "") then 
		return
	end
	
	local profileView = LitheGUI:GetProfileView()
	
	local litheData = LitheDatabase[targetName] or nil
	if (litheData == nil) then
		CHAT_SYSTEM:AddMessage("No profile in database for " .. targetName .. ".")
		profileView:SetVisible(false)
	else
		profileView:SetActualName(targetName)
		local displayName = litheData.nm or targetName
		if (displayName == "") then
			displayName = targetName
		end
		profileView:SetDisplayName(displayName)
		profileView:SetProfileText(litheData.pf or "")
		
		profileView:SetVisible(true)
	end
end

function LitheEngine:InitializeNameRewrite()
	-- TODO: In the long run, we probably want to modify the UnitFrame stuff directly, not hijack GetUnitName().
	-- But w/e, too lazy to do now.
	
	-- TODO: Changing unit name messes with addons that use it for profile config!
	-- Can't do this. Maybe change the title - CustomTitles does it, so we can do it too!
	-- Otherwise, going to have to change UnitFrame stuff.
	--[[
	local GetOriginalUnitName = GetUnitName
	GetUnitName = function(unitTag)
		if (not IsUnitPlayer(unitTag)) then
			return GetOriginalUnitName(unitTag)
		end
		
		local originalName = GetOriginalUnitName(unitTag)
		local litheData = LitheDatabase[originalName] or nil
		
		if (litheData ~= nil) then
			local newName = litheData.nm or ""
			if (newName == "") then
				newName = originalName
			end
			
			return "|c00ffff" .. newName .. "|r"
		end
		
		return originalName
	end
	]]--
end


function LitheEngine.OnAddOnLoaded(event, addonName)
	-- The event fires each time *any* addon loads - but we only care about when our own addon loads.
	if (addonName == ADDON_NAME) then
		LitheEngine:Initialize()
		EVENT_MANAGER:UnregisterForEvent(ADDON_NAME, EVENT_ADD_ON_LOADED)
	end
end
EVENT_MANAGER:RegisterForEvent(ADDON_NAME, EVENT_ADD_ON_LOADED, LitheEngine.OnAddOnLoaded)