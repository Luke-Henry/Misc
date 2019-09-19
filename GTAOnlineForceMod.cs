using System;
using System.Drawing;
using System.Windows.Forms;
using GTA;
using GTA.Native;

public class GTAOnlineForceMod : Script
{
    public GTAOnlineForceMod()
    {
        this.KeyUp += onKeyUp;
    }

    private void onKeyUp(object sender, KeyEventArgs e)
    {
        Ped player = Game.Player.Character;
        Weapon currentWeapon = player.Weapons.Current;
        if (e.KeyCode == Keys.NumPad9)
        {
            if (e.KeyCode == Keys.NumPad9)
            {
                Vehicle vehicle = World.CreateVehicle(VehicleHash.Hakuchou, Game.Player.Character.Position + Game.Player.Character.ForwardVector * 5.0f, Game.Player.Character.Heading);
                Function.Call(Hash.SET_VEHICLE_MOD_KIT, vehicle.Handle, 0);
                vehicle.PlaceOnGround();
                vehicle.SetMod(VehicleMod.BackWheels, 7, true);
                vehicle.SetMod(VehicleMod.FrontWheels, 7, true);
                vehicle.SetMod(VehicleMod.Transmission, 2, false);
                vehicle.SetMod(VehicleMod.Armor, 4, false);
                vehicle.SetMod(VehicleMod.Engine, 3, false);
                vehicle.SetMod(VehicleMod.Brakes, 2, false);
                vehicle.SetMod(VehicleMod.Suspension, 3, false);
                vehicle.ClearCustomPrimaryColor();
                vehicle.ClearCustomSecondaryColor();
                vehicle.PrimaryColor = VehicleColor.MetallicClassicGold;
                vehicle.SecondaryColor = VehicleColor.MetallicClassicGold;
                vehicle.NumberPlate = "REKT";
                vehicle.IsStolen = false;
                vehicle.IsPersistent = false;
                vehicle.EngineRunning = true;
                vehicle.ToggleMod(VehicleToggleMod.Turbo, true);
                vehicle.CanTiresBurst = false;
                vehicle.IsVisible = true;
                vehicle.LightsMultiplier = 5.0f;
                vehicle.LightsOn = true;
                vehicle.PearlescentColor = VehicleColor.Chrome;
                vehicle.TireSmokeColor = Color.Red;
                player.Task.ClearAllImmediately();
                player.Task.WarpIntoVehicle(vehicle, VehicleSeat.Driver);
                player.CanBeKnockedOffBike = false;
                player.CanBeDraggedOutOfVehicle = false;
            }
        }
        if (e.KeyCode == Keys.Divide)
        {
            Vehicle vehicle2 = player.CurrentVehicle;
            Function.Call(Hash.SET_VEHICLE_MOD_KIT, vehicle2.Handle, 0);
            vehicle2.SetMod(VehicleMod.BackWheels, 7, true);
            vehicle2.SetMod(VehicleMod.FrontWheels, 7, true);
            vehicle2.SetMod(VehicleMod.Transmission, 2, false);
            vehicle2.SetMod(VehicleMod.Armor, 4, false);
            vehicle2.SetMod(VehicleMod.Engine, 3, false);
            vehicle2.SetMod(VehicleMod.Brakes, 2, false);
            vehicle2.SetMod(VehicleMod.Suspension, 3, false);
            vehicle2.ClearCustomPrimaryColor();
            vehicle2.ClearCustomSecondaryColor();
            vehicle2.PrimaryColor = VehicleColor.HotPink;
            vehicle2.SecondaryColor = VehicleColor.HotPink;
            vehicle2.CanTiresBurst = false;
            vehicle2.ToggleMod(VehicleToggleMod.Turbo, true);
            vehicle2.NumberPlate = "REKT";
            vehicle2.IsInvincible = true;
            vehicle2.PreviouslyOwnedByPlayer = true;
            vehicle2.IsStolen = false;
        }
        if (e.KeyCode == Keys.Add)
        {
            if (player.IsInVehicle() == true)
            {
                Vehicle vehicle3 = player.CurrentVehicle;
                vehicle3.Speed = vehicle3.Speed + 20.0f;
            }
            else
            {
                player.Velocity = player.Velocity * 20.0f;
                Wait(300);
                player.Velocity = player.Velocity * 0.1f;
            }
        }
        if (e.KeyCode == Keys.Subtract)
        {
            player.IsInvincible = true;
        }
        if (e.KeyCode == Keys.Multiply)
        {
            player.IsInvincible = false;
        }
        if (e.KeyCode == Keys.D0)
        {
            Vehicle vehicle4 = World.CreateVehicle(VehicleHash.Savage, Game.Player.Character.Position + Game.Player.Character.ForwardVector * 5.0f, Game.Player.Character.Heading + 90);
            vehicle4.PlaceOnGround();
            vehicle4.Health = 99999999;
            vehicle4.IsStolen = false;
            vehicle4.ClearCustomPrimaryColor();
            vehicle4.ClearCustomSecondaryColor();
            vehicle4.PrimaryColor = VehicleColor.HotPink;
            vehicle4.SecondaryColor = VehicleColor.HotPink;
            vehicle4.EngineRunning = true;
            vehicle4.IsPersistent = false;
            vehicle4.IsInvincible = true;
            vehicle4.CanBeVisiblyDamaged = false;
            vehicle4.PreviouslyOwnedByPlayer = true;
        }
        if (e.KeyCode == Keys.Decimal)
        {
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 10.0f, ExplosionType.BigExplosion1, 9999.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 10.0f, ExplosionType.BigExplosion1, 100.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 10.0f, ExplosionType.BigExplosion1, 100.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 10.0f, ExplosionType.BigExplosion1, 100.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 10.0f, ExplosionType.BigExplosion1, 100.0f, 0.0f);
            Wait(100);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 15.0f, ExplosionType.BigExplosion1, 120.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 15.0f, ExplosionType.BigExplosion1, 120.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 15.0f, ExplosionType.BigExplosion1, 120.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 15.0f, ExplosionType.BigExplosion1, 120.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 15.0f, ExplosionType.BigExplosion1, 120.0f, 0.0f);
            Wait(100);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 20.0f, ExplosionType.BigExplosion1, 140.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 20.0f, ExplosionType.BigExplosion1, 140.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 20.0f, ExplosionType.BigExplosion1, 140.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 20.0f, ExplosionType.BigExplosion1, 140.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 20.0f, ExplosionType.BigExplosion1, 140.0f, 0.0f);
            Wait(100);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 25.0f, ExplosionType.BigExplosion1, 160.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 25.0f, ExplosionType.BigExplosion1, 160.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 25.0f, ExplosionType.BigExplosion1, 160.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 25.0f, ExplosionType.BigExplosion1, 160.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 25.0f, ExplosionType.BigExplosion1, 160.0f, 0.0f);
            Wait(100);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 30.0f, ExplosionType.BigExplosion1, 180.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 30.0f, ExplosionType.BigExplosion1, 180.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 30.0f, ExplosionType.BigExplosion1, 180.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 30.0f, ExplosionType.BigExplosion1, 180.0f, 0.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 30.0f, ExplosionType.BigExplosion1, 180.0f, 0.0f);
        }

        if (e.KeyCode == Keys.NumPad2)
        {
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 1.5f + Game.Player.Character.UpVector * -0.5f, ExplosionType.ValveWater1, 10000.0f, 20.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 2.0f + Game.Player.Character.UpVector * -0.5f, ExplosionType.ValveWater1, 10000.0f, 20.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 3.0f + Game.Player.Character.UpVector * -0.5f, ExplosionType.ValveWater1, 10000.0f, 20.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 4.0f + Game.Player.Character.UpVector * -0.5f, ExplosionType.ValveWater1, 10000.0f, 20.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 5.0f + Game.Player.Character.UpVector * -0.5f, ExplosionType.ValveWater1, 10000.0f, 20.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 6.0f + Game.Player.Character.UpVector * -0.5f, ExplosionType.ValveWater1, 10000.0f, 20.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 7.0f + Game.Player.Character.UpVector * -0.5f, ExplosionType.ValveWater1, 10000.0f, 20.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 8.0f + Game.Player.Character.UpVector * -0.5f, ExplosionType.ValveWater1, 10000.0f, 20.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 9.0f + Game.Player.Character.UpVector * -0.5f, ExplosionType.ValveWater1, 10000.0f, 20.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 10.0f + Game.Player.Character.UpVector * -0.5f, ExplosionType.ValveWater1, 10000.0f, 20.0f);
        }

        if (e.KeyCode == Keys.End)
        {
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 1.5f + Game.Player.Character.UpVector * -0.8f, ExplosionType.ValveFire2, 10000.0f, 20.0f);
            World.AddExplosion(Game.Player.Character.Position + Game.Player.Character.ForwardVector * 3.0f + Game.Player.Character.UpVector * -0.8f, ExplosionType.ValveFire2, 10000.0f, 20.0f);
        }

        if (e.KeyCode == Keys.Oemcomma)
        {
            currentWeapon.AmmoInClip = currentWeapon.MaxAmmoInClip;
            currentWeapon.Ammo = currentWeapon.MaxAmmo;
            Game.Player.WantedLevel = 0;
            player.FiringPattern = FiringPattern.FullAuto;
        }
        if (e.KeyCode == Keys.OemSemicolon)
        {
            Vehicle[] vehicle6 = World.GetNearbyVehicles(player, 5000000.0f);
            player.Task.WarpIntoVehicle(vehicle6[1], VehicleSeat.Any);
            player.Task.WarpIntoVehicle(vehicle6[1], VehicleSeat.Passenger);
            player.Task.WarpIntoVehicle(vehicle6[1], VehicleSeat.Driver);
        }
        if (e.KeyCode == Keys.OemQuestion)
        {
            Vehicle[] vehicle7 = World.GetNearbyVehicles(player, 5000000.0f);
            player.Task.WarpIntoVehicle(vehicle7[1], VehicleSeat.Any);
            player.Task.WarpIntoVehicle(vehicle7[1], VehicleSeat.Passenger);
            player.Task.WarpIntoVehicle(vehicle7[1], VehicleSeat.Any);
            Wait(100);
            Vehicle vehicle8 = player.CurrentVehicle;
            vehicle8.ClearCustomPrimaryColor();
            vehicle8.ClearCustomSecondaryColor();
            vehicle8.PrimaryColor = VehicleColor.HotPink;
            vehicle8.SecondaryColor = VehicleColor.HotPink;
            vehicle8.NumberPlate = "REKT";
            vehicle8.IsStolen = true;
            vehicle8.IsWanted = true;
            vehicle8.Speed = 50.0f;
        }
    }
}