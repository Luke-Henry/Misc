using System;
using System.Drawing;
using System.Windows.Forms;
using GTA;
using GTA.Native;

public class GTAOnlineToggleInvis : Script
{
    public GTAOnlineToggleInvis()
    {
        this.KeyDown += onKeyDown;
    }


    private void onKeyDown(object sender, KeyEventArgs e)
    {
        Ped player2 = Game.Player.Character;
        int toggle = 0;
        if (e.KeyCode == Keys.OemPeriod)
        {
            toggle = 1;
            while (toggle == 1)
            {
                player2.IsVisible = false;
                Wait(20);
                if (e.KeyCode == Keys.OemPeriod)
                {
                    toggle = 0;
                }
            }
        }
    }
}