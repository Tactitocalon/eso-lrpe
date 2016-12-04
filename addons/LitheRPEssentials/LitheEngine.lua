LitheEngine = LitheEngine or {}
LitheDatabase = LitheDatabase or {}

-- Addon constants
local ADDON_NAME = "LitheRPEssentials"
local ADDON_VERSION = 1

-- Library registration
local LIBCHAT = LibStub('libChat-1.0')

function LitheEngine:Initialize()
	SLASH_COMMANDS["/examine"] = LitheEngine.OnCommandExamine
	LitheEngine:InitializeNameRewrite()
end


 
function LitheEngine.OnCommandExamine(commandData)
	-- Extract the name that we are examining
	local targetName = commandData or ""
	-- Trim string
	targetName = string.gsub(targetName , "%s$", "")
	
	if (targetName == "") then return end

	local litheData = LitheDatabase[targetName] or nil
	if (litheData == nil) then
		CHAT_SYSTEM:AddMessage("No profile in database for " .. targetName .. ".")
	else
		CHAT_SYSTEM:AddMessage("Profile for " .. targetName .. ":")
		CHAT_SYSTEM:AddMessage("|cffffff" .. (litheData.pf or "") .. "|r")
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