--[[

This Add-on is not created by, affiliated with or sponsored by ZeniMax
Media Inc. or its affiliates. The Elder Scrolls® and related logos are
registered trademarks or trademarks of ZeniMax Media Inc. in the United
States and/or other countries. All rights reserved.
You can read the full terms at https://account.elderscrollsonline.com/add-on-terms

This software is under : CreativeCommons CC BY-NC-SA 4.0
Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)

You are free to:

    Share — copy and redistribute the material in any medium or format
    Adapt — remix, transform, and build upon the material
    The licensor cannot revoke these freedoms as long as you follow the license terms.


Under the following terms:

    Attribution — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
    NonCommercial — You may not use the material for commercial purposes.
    ShareAlike — If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.
    No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.


Please read full licence at : 
http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode


LibChat2 is a library which must be embedded into an ESO Addon, throught its manifest like this :
path/libChat2/libChat2.lua

LibChat2 require LibStub to work

Author: Ayantir
Filename: libChat2.lua
Version: 9

-----

CHAT_SYSTEM does not permit to get 2 libraries running together and using ChatBox, the first loaded into memory will rewrite EVERYTHING and other addons will fail

- If you REWRITE message, you're hugely prompted to use LibChat2, without it, you'll surely kill other chat Addons
	eg: Append some text, rewrite sender name, rewrite colors, etc
	
- DO NOT USE this library if you don't REWRITE the message sent
- If you only APPEND something with a d() or a AddMessage(), you don't need LibChat2


WARNING using registerFormat() : This method should only be called when rewriting from + text + infos (colors, chanCode, etc), please only use this method if your addon REWRITE the WHOLE message
If you need to rewrite From, please only use registerName
If you need to rewrite Text, please only use registerText
If you need to append text somewhere without changing the text sent, please use one of the following methods :
DDSBeforeAll, TextBeforeAll, DDSBeforeSender, TextBeforeSender, TextAfterSender, DDSAfterSender, DDSBeforeText, TextBeforeText, TextAfterText, DDSAfterText

Revisions of libChat

Minor = 1 : libChat-1
Minor = 2 : LibChat2 1.0
Minor = 3 : LibChat2 1.1
Minor = 4 : LibChat2 1.2
Minor = 5 : LibChat2 1.3 (internal release)
Minor = 6 : LibChat2 Justice
Minor = 7 : LibChat2 1.6
Minor = 8 : LibChat2 8
Minor = 9 : LibChat2 9

More info : http://www.esoui.com/downloads/info740-libChat2.html

]]--

local LIB_NAME, LIB_VERSION = "libChat-1.0", 9

local libchat, oldminor = LibStub:NewLibrary(LIB_NAME, LIB_VERSION)
if not libchat then
	return
end

-- local declaration
local funcName
local funcText
local funcFormat

local funcFriendStatus
local funcIgnoreAdd
local funcIgnoreRemove
local funcGroupMemberLeft
local funcGroupTypeChanged

local funcDDSBeforeAll
local funcTextBeforeAll
local funcDDSBeforeSender
local funcTextBeforeSender
local funcDDSAfterSender
local funcTextAfterSender
local funcDDSBeforeText
local funcTextBeforeText
local funcTextAfterText
local funcDDSAfterText

-- Initialize Manager to trace Addons
if not libchat.manager then
	libchat.manager = {}
end

-- Returns ZOS CustomerService Icon if needed
local function showCustomerService(isCustomerService)

	if(isCustomerService) then
		return "|t16:16:EsoUI/Art/ChatWindow/csIcon.dds|t"
	end
	
	return ""
	
end

-- Listens for EVENT_CHAT_MESSAGE_CHANNEL event from ZO_ChatSystem
local function libChatMessageChannelReceiver(channelID, from, text, isCustomerService, fromDisplayName)
	
	local message
	local DDSBeforeAll = ""
	local TextBeforeAll = ""
	local DDSBeforeSender = ""
	local TextBeforeSender = ""
	local DDSAfterSender = ""
	local TextAfterSender = ""
	local DDSBeforeText = ""
	local TextBeforeText = ""
	local TextAfterText = ""
	local DDSAfterText = ""
	local originalFrom = from
	local originalText = text
	
	-- Get channel information
	local ChanInfoArray = ZO_ChatSystem_GetChannelInfo()
	local info = ChanInfoArray[channelID]
	
	if not info or not info.format then
		return
	end
	
	-- Function to append
	if funcDDSBeforeAll then
		DDSBeforeAll = funcDDSBeforeAll(channelID, from, text, isCustomerService, fromDisplayName)
	end
	
	-- Function to append
	if funcTextBeforeAll then
		TextBeforeAll = funcTextBeforeAll(channelID, from, text, isCustomerService, fromDisplayName)
	end
	
	-- Function to append
	if funcDDSBeforeSender then
		DDSBeforeSender = funcDDSBeforeSender(channelID, from, text, isCustomerService, fromDisplayName)
	end
	
	-- Function to append
	if funcTextBeforeSender then
		TextBeforeSender = funcTextBeforeSender(channelID, from, text, isCustomerService, fromDisplayName)
	end
	
	-- Function to append
	if funcDDSAfterSender then
		DDSAfterSender = funcDDSAfterSender(channelID, from, text, isCustomerService, fromDisplayName)
	end
	
	-- Function to append
	if funcTextAfterSender then
		TextAfterSender = funcTextAfterSender(channelID, from, text, isCustomerService, fromDisplayName)
	end
	
	-- Function to append
	if funcDDSBeforeText then
		DDSBeforeText = funcDDSBeforeText(channelID, from, text, isCustomerService, fromDisplayName)
	end
	
	-- Function to append
	if funcTextBeforeText then
		TextBeforeText = funcTextBeforeText(channelID, from, text, isCustomerService, fromDisplayName)
	end
	
	-- Function to append
	if funcTextAfterText then
		TextAfterText = funcTextAfterText(channelID, from, text, isCustomerService, fromDisplayName)
	end
	
	-- Function to append
	if funcDDSAfterText then
		DDSAfterText = funcDDSAfterText(channelID, from, text, isCustomerService, fromDisplayName)
	end

	-- Function to affect From
	if funcName then
		from = funcName(channelID, from, isCustomerService, fromDisplayName)
		if not from then return	end
	end
	
	-- Function to format text
	if funcText then
		text = funcText(channelID, from, text, isCustomerService, fromDisplayName)
		if not text then return end
	end
	
	-- Function to format message
	if funcFormat then
		message = funcFormat(channelID, from, text, isCustomerService, fromDisplayName, originalFrom, originalText, DDSBeforeAll, TextBeforeAll, DDSBeforeSender, TextBeforeSender, TextAfterSender, DDSAfterSender, DDSBeforeText, TextBeforeText, TextAfterText, DDSAfterText)
		if not message then return end
	else
	
		-- Code to run with libChat loaded and Addon not registered to libchat - IT MUST BE ~SAME~ AS ESOUI -
		
		-- Create channel link
		local channelLink
		if info.channelLinkable then
			local channelName = GetChannelName(info.id)
			channelLink = ZO_LinkHandler_CreateChannelLink(channelName)
		end
		
		-- Create player link
		local playerLink
		if info.playerLinkable and not from:find("%[") then
			playerLink = DDSBeforeSender .. TextBeforeSender .. ZO_LinkHandler_CreatePlayerLink((from)) .. TextAfterSender .. DDSAfterSender
		else
			playerLink = DDSBeforeSender .. TextBeforeSender .. from .. TextAfterSender .. DDSAfterSender
		end
		
		text = DDSBeforeText .. TextBeforeText .. text .. TextAfterText .. DDSAfterText
		
		-- Create default formatting
		if channelLink then
			message = DDSBeforeAll .. TextBeforeAll .. zo_strformat(info.format, channelLink, playerLink, text)
		else
			message = DDSBeforeAll .. TextBeforeAll .. zo_strformat(info.format, playerLink, text, showCustomerService(isCustomerService))
		end
	end
	
	return message, info.saveTarget
	
end

-- Listens for EVENT_FRIEND_PLAYER_STATUS_CHANGED event from ZO_ChatSystem
local function libChatFriendPlayerStatusChangedReceiver(displayName, characterName, oldStatus, newStatus)
	
	-- If function registrered in Addon, code will run
	local friendStatusMessage
	
	if funcFriendStatus then
		friendStatusMessage = funcFriendStatus(displayName, characterName, oldStatus, newStatus)
		if friendStatusMessage then
			return friendStatusMessage
		else
			return
		end
	else
	
		-- Code to run with libChat loaded and Addon not registered to libchat - IT MUST BE ~SAME~ AS ESOUI -
	
		local wasOnline = oldStatus ~= PLAYER_STATUS_OFFLINE
		local isOnline = newStatus ~= PLAYER_STATUS_OFFLINE
		
		-- DisplayName is linkable
		local displayNameLink = ZO_LinkHandler_CreateDisplayNameLink(displayName)
		-- CharacterName is linkable
		local characterNameLink = ZO_LinkHandler_CreateCharacterLink(characterName)
		
		-- Not connected before and Connected now (no messages for Away/Busy)
		if(not wasOnline and isOnline) then
			-- Return
			return zo_strformat(SI_FRIENDS_LIST_FRIEND_CHARACTER_LOGGED_ON, displayNameLink, characterNameLink)
		-- Connected before and Offline now
		elseif(wasOnline and not isOnline) then
			return zo_strformat(SI_FRIENDS_LIST_FRIEND_CHARACTER_LOGGED_OFF, displayNameLink, characterNameLink)
		end
		
	end
	
end

-- Listens for EVENT_IGNORE_ADDED event from ZO_ChatSystem
local function libChatIgnoreAddedReceiver(displayName)
	
	-- If function registrered in Addon, code will run
	local ignoreAddMessage
	
	if funcIgnoreAdd then
		ignoreAddMessage = funcIgnoreAdd(displayName)
		if ignoreAddMessage then
			return ignoreAddMessage
		else
			return
		end
	else
	
		-- Code to run with libChat loaded and Addon not registered to libchat - IT MUST BE ~SAME~ AS ESOUI -
		
		-- DisplayName is linkable
		local displayNameLink = ZO_LinkHandler_CreateDisplayNameLink(displayName)
		ignoreAddMessage = zo_strformat(SI_FRIENDS_LIST_IGNORE_ADDED, displayNameLink)
		
	end
	
	return ignoreAddMessage
	
end

-- Listens for EVENT_IGNORE_REMOVED event from ZO_ChatSystem
local function libChatIgnoreRemovedReceiver(displayName)
	
	-- If function registrered in Addon, code will run
	local ignoreRemoveMessage
	
	if funcIgnoreRemove then
		ignoreRemoveMessage = funcIgnoreRemove(displayName)
		if ignoreRemoveMessage then
			return ignoreRemoveMessage
		else
			return
		end
	else
	
		-- Code to run with libChat loaded and Addon not registered to libchat - IT MUST BE ~SAME~ AS ESOUI -
		
		-- DisplayName is linkable
		local displayNameLink = ZO_LinkHandler_CreateDisplayNameLink(displayName)
		ignoreRemoveMessage = zo_strformat(SI_FRIENDS_LIST_IGNORE_REMOVED, displayNameLink)
		
	end
	
	return ignoreRemoveMessage
	
end

-- Listens for EVENT_GROUP_MEMBER_LEFT event from ZO_ChatSystem
local function libChatGroupMemberLeftReceiver(characterName, reason, isLocalPlayer, isLeader, memberDisplayName, actionRequiredVote)
	
	-- If function registrered in Addon, code will run
	local groupMemberLeftMessage
	
	if funcGroupMemberLeft then
		groupMemberLeftMessage = funcGroupMemberLeft(characterName, reason, isLocalPlayer, isLeader, memberDisplayName, actionRequiredVote)
		if groupMemberLeftMessage then
			return groupMemberLeftMessage
		else
			return
		end
	else
	
		-- Code to run with libChat loaded and Addon not registered to libchat - IT MUST BE ~SAME~ AS ESOUI -
		if reason == GROUP_LEAVE_REASON_KICKED and isLocalPlayer and actionRequiredVote then
			groupMemberLeftMessage = GetString(SI_GROUP_ELECTION_KICK_PLAYER_PASSED)
		end
		
	end
	
	return groupMemberLeftMessage
	
end

-- Listens for EVENT_GROUP_TYPE_CHANGED event from ZO_ChatSystem
local function libChatGroupTypeChangedReceiver(largeGroup)
	
	-- If function registrered in Addon, code will run
	local GroupTypeChangedMessage
	
	if funcGroupTypeChanged then
		GroupTypeChangedMessage = funcGroupTypeChanged(largeGroup)
		if GroupTypeChangedMessage then
			return GroupTypeChangedMessage
		else
			return
		end
	else
	
		-- Code to run with libChat loaded and Addon not registered to libchat - IT MUST BE ~SAME~ AS ESOUI -
		
        if largeGroup then
            return GetString(SI_CHAT_ANNOUNCEMENT_IN_LARGE_GROUP)
        else
            return GetString(SI_CHAT_ANNOUNCEMENT_IN_SMALL_GROUP)
        end
		
	end
	
end

local function registerFunction(addonFunc, funcToUse, ...)

	if funcToUse == "registerName" then
		funcName = addonFunc
	elseif funcToUse == "registerText" then
		funcText = addonFunc
	elseif funcToUse == "registerFormat" then
		funcFormat = addonFunc
	elseif funcToUse == "registerFriendStatus" then
		funcFriendStatus = addonFunc
	elseif funcToUse == "registerIgnoreAdd" then
		funcIgnoreAdd = addonFunc
	elseif funcToUse == "registerIgnoreRemove" then
		funcIgnoreRemove = addonFunc
	elseif funcToUse == "registerGroupMemberLeft" then
		funcGroupMemberLeft = addonFunc
	elseif funcToUse == "registerGroupTypeChanged" then
		funcGroupTypeChanged = addonFunc
	elseif funcToUse == "registerAppendDDSBeforeAll" then
		funcDDSBeforeAll = addonFunc
	elseif funcToUse == "registerAppendTextBeforeAll" then
		funcTextBeforeAll = addonFunc
	elseif funcToUse == "registerAppendDDSBeforeSender" then
		funcDDSBeforeSender = addonFunc
	elseif funcToUse == "registerAppendTextBeforeSender" then
		funcTextBeforeSender = addonFunc
	elseif funcToUse == "registerAppendDDSAfterSender" then
		funcDDSAfterSender = addonFunc
	elseif funcToUse == "registerAppendTextAfterSender" then
		funcTextAfterSender = addonFunc
	elseif funcToUse == "registerAppendDDSBeforeText" then
		funcDDSBeforeText = addonFunc
	elseif funcToUse == "registerAppendTextBeforeText" then
		funcTextBeforeText = addonFunc
	elseif funcToUse == "registerAppendTextAfterText" then
		funcTextAfterText = addonFunc
	elseif funcToUse == "registerAppendDDSAfterText" then
		funcDDSAfterText = addonFunc
	end
	
	if not libchat.manager[funcToUse] then
		libchat.manager[funcToUse] = {}
	end
	
	-- Adding the registration to manager
	local addonName = select(1, ...)
	-- AddonName registered!
	if addonName then
		if type(addonName) == "string" then
			table.insert(libchat.manager[funcToUse], addonName)
		else
			table.insert(libchat.manager[funcToUse],"Anonymous AddOn")
		end
	-- AddonName not set, so.. Anonymous
	else
		table.insert(libchat.manager[funcToUse],"Anonymous AddOn")
	end
	
end

-- Register a function to be called to modify MessageChannel Sender Name
function libchat:registerName(func, ...)
	registerFunction(func, "registerName", ...)
end

-- Register a function to be called to modify MessageChannel Text
function libchat:registerText(func, ...)
	registerFunction(func, "registerText", ...)
end

-- Register a function to be called to format MessageChannel whole Message
function libchat:registerFormat(func, ...)
	registerFunction(func, "registerFormat", ...)
end

-- Register a function to be called to format FriendStatus Message
function libchat:registerFriendStatus(func, ...)
	registerFunction(func, "registerFriendStatus", ...)
end

-- Register a function to be called to format IgnoreAdd Message
function libchat:registerIgnoreAdd(func, ...)
	registerFunction(func, "registerIgnoreAdd", ...)
end

-- Register a function to be called to format IgnoreRemove Message
function libchat:registerIgnoreRemove(func, ...)
	registerFunction(func, "registerIgnoreRemove", ...)
end

-- Register a function to be called to format GroupTypeChanged Message
function libchat:registerGroupMemberLeft(func, ...)
	registerFunction(func, "registerGroupMemberLeft", ...)
end

-- Register a function to be called to format GroupTypeChanged Message
function libchat:registerGroupTypeChanged(func, ...)
	registerFunction(func, "registerGroupTypeChanged", ...)
end

-- register a function to be called to format MessageChannel Message
function libchat:registerAppendDDSBeforeAll(func, ...)
	registerFunction(func, "registerAppendDDSBeforeAll", ...)
end

-- register a function to be called to format MessageChannel Message
function libchat:registerAppendTextBeforeAll(func, ...)
	registerFunction(func, "registerAppendTextBeforeAll", ...)
end

-- register a function to be called to format MessageChannel Message
function libchat:registerAppendDDSBeforeSender(func, ...)
	registerFunction(func, "registerAppendDDSBeforeSender", ...)
end

-- register a function to be called to format MessageChannel Message
function libchat:registerAppendTextBeforeSender(func, ...)
	registerFunction(func, "registerAppendTextBeforeSender", ...)
end

-- register a function to be called to format MessageChannel Message
function libchat:registerAppendDDSAfterSender(func, ...)
	registerFunction(func, "registerAppendDDSAfterSender", ...)
end

-- register a function to be called to format MessageChannel Message
function libchat:registerAppendTextAfterSender(func, ...)
	registerFunction(func, "registerAppendTextAfterSender", ...)
end

-- register a function to be called to format MessageChannel Message
function libchat:registerAppendDDSBeforeText(func, ...)
	registerFunction(func, "registerAppendDDSBeforeText", ...)
end

-- register a function to be called to format MessageChannel Message
function libchat:registerAppendTextBeforeText(func, ...)
	registerFunction(func, "registerAppendTextBeforeText", ...)
end

-- register a function to be called to format MessageChannel Message
function libchat:registerAppendTextAfterText(func, ...)
	registerFunction(func, "registerAppendTextAfterText", ...)
end

-- register a function to be called to format MessageChannel Message
function libchat:registerAppendDDSAfterText(func, ...)
	registerFunction(func, "registerAppendDDSAfterText", ...)
end

local function libchatdebug()
	
	local message
	
	CHAT_SYSTEM:AddMessage("---- libchat2 debug ----")
	CHAT_SYSTEM:AddMessage("Note : 2 addons registering same method will provoke conflicts")
	
	for keymanager, subarray in pairs(libchat.manager) do
		message = keymanager .. " set with addon "
		if keymanager == "registerFormat" then 
			message = message .. "WARNING : method overwrite Sender name and Message !"
		end
		for addonIndex, addonName in ipairs(subarray) do message = message .. " #" .. addonIndex .. " " .. addonName .. "," end
		CHAT_SYSTEM:AddMessage(message)
	end
	
	CHAT_SYSTEM:AddMessage("---- end of libchat2 debug ----")
	
end
 
SLASH_COMMANDS["/libchat"] = libchatdebug

-- AddEventHandler to ZO_ChatSystem with same name than the original one cause the Event triggers library instead of ESOUI
ZO_ChatSystem_AddEventHandler(EVENT_CHAT_MESSAGE_CHANNEL, libChatMessageChannelReceiver)
ZO_ChatSystem_AddEventHandler(EVENT_FRIEND_PLAYER_STATUS_CHANGED, libChatFriendPlayerStatusChangedReceiver)
ZO_ChatSystem_AddEventHandler(EVENT_IGNORE_ADDED, libChatIgnoreAddedReceiver)
ZO_ChatSystem_AddEventHandler(EVENT_IGNORE_REMOVED, libChatIgnoreRemovedReceiver)

ZO_ChatSystem_AddEventHandler(EVENT_GROUP_MEMBER_LEFT, libChatGroupMemberLeftReceiver)
ZO_ChatSystem_AddEventHandler(EVENT_GROUP_TYPE_CHANGED, libChatGroupTypeChangedReceiver)

--[[

Not Yet

ZO_ChatSystem_AddEventHandler(EVENT_SERVER_SHUTDOWN_INFO, libChatFriendPlayerStatusChangedReceiver)
ZO_ChatSystem_AddEventHandler(EVENT_BROADCAST, libChatFriendPlayerStatusChangedReceiver)
ZO_ChatSystem_AddEventHandler(EVENT_QUEUE_FOR_CAMPAIGN_RESPONSE, libChatFriendPlayerStatusChangedReceiver)

ZO_ChatSystem_AddEventHandler(EVENT_STUCK_ERROR_ON_COOLDOWN, libChatFriendPlayerStatusChangedReceiver)
ZO_ChatSystem_AddEventHandler(EVENT_STUCK_ERROR_ALREADY_IN_PROGRESS, libChatFriendPlayerStatusChangedReceiver)
ZO_ChatSystem_AddEventHandler(EVENT_STUCK_ERROR_IN_COMBAT, libChatFriendPlayerStatusChangedReceiver)
ZO_ChatSystem_AddEventHandler(EVENT_STUCK_ERROR_INVALID_LOCATION, libChatFriendPlayerStatusChangedReceiver)
]]--