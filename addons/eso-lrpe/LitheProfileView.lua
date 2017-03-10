LitheGUI = LitheGUI or {}

local WINDOW_MANAGER = GetWindowManager()
local LIB_MSGWIN = LibStub:GetLibrary("LibMsgWin-1.0")

local LitheProfileView = LitheProfileView or {}

local DIM_WIDTH = 800
local DIM_HEIGHT = math.min(1000, GuiRoot:GetHeight() * 0.8)

function LitheGUI:GetProfileView()
	-- Lazy init of LitheProfileView
	if (LitheProfileView.window == nil) then
		-- Create window
		LitheProfileView.window = WINDOW_MANAGER:CreateTopLevelWindow("litheProfileViewWindow")	
		LitheProfileView.window:SetDimensions(DIM_WIDTH + 8, DIM_HEIGHT + 8)
		LitheProfileView.window:SetAnchor(CENTER, GuiRoot, CENTER, 0, 0)
		LitheProfileView.window:SetMovable(true)
		LitheProfileView.window:SetMouseEnabled(true)
		LitheProfileView.window:SetClampedToScreenInsets(100)
		LitheProfileView.window:SetDimensionConstraints(300, 200)
		LitheProfileView.window:SetResizeHandleSize(8)
		
		-- Create backdrop
		LitheProfileView.bdBackdrop = WINDOW_MANAGER:CreateControlFromVirtual("bdBackdrop", LitheProfileView.window, "ZO_DefaultBackdrop")
		LitheProfileView.bdBackdrop:SetAnchor(TOPLEFT, LitheProfileView.window, TOPLEFT, 4, 4)
		LitheProfileView.bdBackdrop:SetAnchor(BOTTOMRIGHT, LitheProfileView.window, BOTTOMRIGHT, -4, -4)
		LitheProfileView.bdBackdrop:SetAlpha(0.6)
		-- LitheProfileView.bdBackdrop:SetDimensions(DIM_WIDTH, DIM_HEIGHT)
		
		-- Create header label
		LitheProfileView.lblActualName = WINDOW_MANAGER:CreateControl("lblActualName", LitheProfileView.window, CT_LABEL)
		LitheProfileView.lblActualName:SetColor(0.8, 0.8, 0.8, 1)
		LitheProfileView.lblActualName:SetFont("ZoFontHeader")
		LitheProfileView.lblActualName:SetScale(1)
		LitheProfileView.lblActualName:SetWrapMode(TEX_MODE_CLAMP)
		LitheProfileView.lblActualName:SetDrawLayer(1)
		LitheProfileView.lblActualName:SetText("Profile for [CharacterName]")
		LitheProfileView.lblActualName:SetAnchor(TOPLEFT, LitheProfileView.window, TOPLEFT, 25, 25)
		LitheProfileView.lblActualName:SetDimensions(DIM_WIDTH - 50, 20)
		
		-- Create display name label
		LitheProfileView.lblCharacterName = WINDOW_MANAGER:CreateControl("lblCharacterName", LitheProfileView.window, CT_LABEL)
		LitheProfileView.lblCharacterName:SetColor(1, 1, 1, 1)
		LitheProfileView.lblCharacterName:SetFont("ZoFontHeader3")
		LitheProfileView.lblCharacterName:SetScale(1)
		LitheProfileView.lblCharacterName:SetWrapMode(TEX_MODE_CLAMP)
		LitheProfileView.lblCharacterName:SetDrawLayer(1)
		LitheProfileView.lblCharacterName:SetText("[DisplayName]")
		LitheProfileView.lblCharacterName:SetAnchor(TOPLEFT, LitheProfileView.window, TOPLEFT, 25, 50)
		LitheProfileView.lblCharacterName:SetDimensions(DIM_WIDTH - 50, 20)
		
		-- Create profile data text control		
		LitheProfileView.bgProfileData = WINDOW_MANAGER:CreateControl("bgProfileData", LitheProfileView.window, CT_BACKDROP)
		LitheProfileView.bgProfileData:SetEdgeColor(0, 0, 0)
		LitheProfileView.bgProfileData:SetCenterColor(0, 0, 0)
		LitheProfileView.bgProfileData:SetAnchor(TOPLEFT, LitheProfileView.window, TOPLEFT, 25, 85)
		LitheProfileView.bgProfileData:SetAnchor(BOTTOMRIGHT, LitheProfileView.window, BOTTOMRIGHT, -25, -25)
		LitheProfileView.bgProfileData:SetAlpha(0.5)
		LitheProfileView.bgProfileData:SetEdgeTexture("", 1, 1, 0)
		LitheProfileView.bgProfileData:SetDrawLayer(2)
		
		LitheProfileView.txtProfileData = WINDOW_MANAGER:CreateControlFromVirtual("txtProfileData", LitheProfileView.window, "ZO_DefaultEditMultiLineForBackdrop")
		LitheProfileView.txtProfileData:SetAnchor(TOPLEFT, LitheProfileView.window, TOPLEFT, 30, 90)
		LitheProfileView.txtProfileData:SetAnchor(BOTTOMRIGHT, LitheProfileView.window, BOTTOMRIGHT, -30, -30)
		LitheProfileView.txtProfileData:SetMaxInputChars(30000)
		LitheProfileView.txtProfileData:SetDrawLayer(4)
		LitheProfileView.txtProfileData:SetEditEnabled(false)
		
		-- Create clickable profile URL link label
		
		-- Create top left close X button
		LitheProfileView.btnClose = WINDOW_MANAGER:CreateControl("btnClose", LitheProfileView.window, CT_BUTTON)
		LitheProfileView.btnClose:SetAnchor(TOPRIGHT, LitheProfileView.window, TOPRIGHT, -4, 12)
		LitheProfileView.btnClose:SetDimensions(32, 32)
		LitheProfileView.btnClose:SetNormalTexture('/esoui/art/buttons/closebutton_up.dds')
		LitheProfileView.btnClose:SetMouseOverTexture('/esoui/art/buttons/closebutton_mouseover.dds')
		LitheProfileView.btnClose:SetHandler("OnClicked", LitheProfileView.Hide)
		LitheProfileView.btnClose:SetState(BSTATE_NORMAL)
	end
	
	return LitheProfileView
end

function LitheProfileView:IsVisible()
	return not (LitheProfileView.window:IsControlHidden())
end

function LitheProfileView:Hide()
	LitheProfileView.window:SetHidden(true)
end

function LitheProfileView:SetVisible(bIsVisible)
	LitheProfileView.window:SetHidden(not bIsVisible)
end

function LitheProfileView:SetActualName(sCharacterName)
	LitheProfileView.lblActualName:SetText("Profile for " .. sCharacterName)
end

function LitheProfileView:SetDisplayName(sDisplayName)
	LitheProfileView.lblCharacterName:SetText(sDisplayName)
end

function LitheProfileView:SetProfileText(sProfileText)
	LitheProfileView.txtProfileData:SetText(sProfileText)
	LitheProfileView.txtProfileData:SetTopLineIndex(0)
	LitheProfileView.txtProfileData:SetCursorPosition(0)
end