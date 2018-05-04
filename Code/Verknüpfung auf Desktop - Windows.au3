$success = FileCreateShortcut(@Scriptdir&"/Muehle.jar", @DesktopDir & "/Muehle.lnk", @ScriptDir, "", "Ein Muehle Computer", @ScriptDir&"\icon.ico")
If $success = 1 Then
   MsgBox(0,"Super!","Die Verknüpfung wurde erstellt!")
ElseIf $success = 0 Then
   MsgBox(0,"Achtung!","Hoppla, da ist etwas schief gelaufen!")
EndIf