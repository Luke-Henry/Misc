import java.awt.Graphics2D;
import java.util.Random;

import com.epicbot.api.ActiveScript;
import com.epicbot.api.GameType;
import com.epicbot.api.Manifest;
import com.epicbot.api.concurrent.Task;
import com.epicbot.api.concurrent.node.Node;
import com.epicbot.api.input.Keyboard;
import com.epicbot.api.input.Mouse;
import com.epicbot.api.os.methods.*;
import com.epicbot.api.os.methods.tab.inventory.Inventory;
import com.epicbot.api.os.methods.widget.Camera;
import com.epicbot.api.os.wrappers.interactive.NPC;
import com.epicbot.api.os.wrappers.node.Item;
import com.epicbot.api.os.wrappers.node.SceneObject;
import com.epicbot.api.util.Filter;
import com.epicbot.api.util.Time;


@Manifest(author = "Luke", game = GameType.OldSchool, name = "Construction", description = "Enters POH, creates oak larders/mahogany tables, unnotes planks and pays butler.")

public class Construction extends ActiveScript {
	@Override
	public boolean onStart() {
		Mouse.setSpeed(3190);
		
		provide(new Make());
		provide(new Antiban());
		
		return true;
	}
	
	@Override
	public void onStop() {
		Game.logout();
	}
	
	private final int initialExp = Skills.Skill.CONSTRUCTION.getExperience();
	private final long startTime = System.currentTimeMillis();
	
	long bankTimer, timePassed = 0;
	
	SceneObject enterPortal, exitPortal, myTable, noTable;
	NPC myButler;

	int ran = 0;
	
	// Changes random factor to avoid detectable repetitious behaviour
	// Runs once per iteration
	public int changeRandom() {
		ran = (int)(30 + (Math.random()) * 30);
		return ran;
	}
	
	public class Make extends Node implements Task {
		
		private final Filter<SceneObject> table = new Filter<SceneObject>() {
			private int[] ids = new int[] { 13297, 13298, 13299, 13300, 13001, 13002 }; 
            public boolean accept(SceneObject table) {

                int i = table.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};
		
		private final Filter<SceneObject> tableSpace = new Filter<SceneObject>() {
			private int[] ids = new int[] { 15298, 15299, 15300, 15301 }; 
            public boolean accept(SceneObject tableSpace) {

                int i = tableSpace.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};
		
		private final Filter<SceneObject> bell = new Filter<SceneObject>() {
			private int[] ids = new int[] { 13307, 13308 }; 
            public boolean accept(SceneObject bell) {

                int i = bell.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};
		
		private final Filter<Item> planks = new Filter<Item>() {
			private int[] ids = new int[] { 8776, 8782 }; 
            public boolean accept(Item planks) {

                int i = planks.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};
		
		private final Filter<NPC> butler = new Filter<NPC>() {
			private int[] ids = new int[] { 228, 229 }; 
            public boolean accept(NPC butler) {

                int i = butler.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};
		
		int timeout = 0;
		int gameTick = 0;
		
		@Override
		public void run() {
			changeRandom();
			
			if(!Walking.isRunEnabled() & Walking.getEnergy() > ran) {
				Walking.setRun(true);
			}
			timeout = 0;
			
			if(Inventory.getCount(planks) > 5) {
				while(Inventory.getCount(planks) > 5 && timeout < 30) {
					timeout++;
					if(SceneEntities.getNearest(table) != null) {
						myTable = SceneEntities.getNearest(table);
						if(myTable.isOnScreen()) {
							myTable.interact("Remove Mahogany Table");
							Time.sleep(300);
							gameTick = 0;
							while(Widgets.get(219).getChild(1).getChild(1) == null && gameTick < 20) {
								Time.sleep(75);
								gameTick++;
							}
							try {
								if(Widgets.get(219).getChild(1).getChild(1).isOnScreen()) {
									Keyboard.sendKey('1');
									Time.sleep(900);
									gameTick = 0;
									while(SceneEntities.getNearest(table) != null && gameTick < 20) {
										Time.sleep(50);
										gameTick++;
									}
								}
							}
							catch(Exception e) {
								System.out.println(e + "\nCheck network & server connection");
							}
						}
						else {
							Walking.walkTileMM(myTable.getLocation());
							Camera.turnTo(myTable);
						}
					}
					else if(SceneEntities.getNearest(tableSpace) != null) {
						noTable = SceneEntities.getNearest(tableSpace);
						if(noTable.isOnScreen() && Calculations.distanceTo(noTable) < 8) {
							noTable.interact("Build");
							gameTick = 0;
							while(!Widgets.get(458).getChild(0).isOnScreen() && gameTick < 20) {
								Time.sleep(75);
								gameTick++;
							}
							if(Widgets.get(458).getChild(0).isOnScreen()) {
								Keyboard.sendKey('6');
								Time.sleep(600);
								while(SceneEntities.getNearest(tableSpace) != null && gameTick < 40) {
									Time.sleep(50);
									gameTick++;
								}
							}
						}
						else {
							Walking.walkTileMM(noTable.getLocation());
							Camera.turnTo(noTable);
						}
					}
				}
			}
			else {
				if(NPCs.getNearest(butler) != null && Calculations.distanceTo(NPCs.getNearest(butler)) < 5) {
					try {
						myButler = NPCs.getNearest(butler);
					}
					catch(Exception e) {
						System.out.println(e + "\nCheck network & server connection");
					}
					if(Inventory.getCount(planks) < 5) {
						Time.sleep(300,600);
						if(Widgets.get(219).getChild(1).getChild(0) == null || !Widgets.get(219).getChild(1).getChild(0).isOnScreen())
							myButler.interact("Talk-to");
						gameTick = 0;
						try {
							while(Inventory.contains(planks) && Inventory.getCount(planks) < 5 && !Widgets.get(219).getChild(1).getChild(1).isOnScreen() && gameTick < 30) {
								Time.sleep(75);
								gameTick++;
							}
						}
						catch(Exception e) {
							System.out.println("Yep, no errors here!");
						}
						try {
							if(Widgets.get(219).getChild(1).getChild(0) != null && Widgets.get(219).getChild(1).getChild(0).isOnScreen()) {
								Keyboard.sendKey('1');
								gameTick = 0;
								while(Inventory.getCount(planks) < 5 && gameTick < 80) {
									Time.sleep(88,100);
									gameTick++;
								}
							}
							else if(Widgets.get(231).getChild(3) != null && Widgets.get(231).getChild(3).isOnScreen()) {
								Widgets.get(231).getChild(3).click(true);
								Keyboard.sendKey('1');
								Widgets.get(231).getChild(3).click(true);
							}
						}
						catch(Exception e) {
							
						}
						
						gameTick = 0;
						while(Inventory.getCount(planks) < 5 && !Widgets.get(231).getChild(2).isOnScreen() && gameTick < 20) {
							Time.sleep(75,100);
							gameTick++;
						}
						if(Widgets.get(231).getChild(2).isOnScreen()) {
							Keyboard.sendKey(32);
							if(SceneEntities.getNearest(table) != null)
								myTable = SceneEntities.getNearest(table);
							if(myTable.isOnScreen()) {
								myTable.interact("Remove Mahogany Table");
								Time.sleep(300);
								gameTick = 0;
								while(Widgets.get(219).getChild(1).getChild(1) == null && gameTick < 20) {
									Time.sleep(75);
									gameTick++;
								}
								try {
									if(Widgets.get(219).getChild(1).getChild(1).isOnScreen()) {
										Keyboard.sendKey('1');
										
										Time.sleep(900);
										gameTick = 0;
										while(SceneEntities.getNearest(table) != null && gameTick < 20) {
											Time.sleep(50,85);
											gameTick++;
										}
									}
								}
								catch(Exception e) {
									System.out.println(e + "\nCheck network & server connection");
								}
							}
							else {
								Walking.walkTileMM(myTable.getLocation());
								Camera.turnTo(myTable);
							}
							gameTick = 0;
							while(Inventory.getCount(planks) < 5 && gameTick < 70) {
								Time.sleep(100,149);
								if(Widgets.get(219).getChild(1).getChild(1).isOnScreen()) {
									Keyboard.sendKey('1');
									Time.sleep(900);
									gameTick = 0;
									while(SceneEntities.getNearest(table) != null && gameTick < 20) {
										Time.sleep(50,85);
										gameTick++;
									}
								}
							}
						}
					}
				}
				else if(Inventory.getCount(planks) < 5 && SceneEntities.getNearest(bell) != null) {
					if(SceneEntities.getNearest(bell).isOnScreen()) {
						SceneEntities.getNearest(bell).interact("Ring");
						Time.sleep(1000,1200);
					}
					else {
						Walking.walkTileMM(SceneEntities.getNearest(bell).getLocation());
						Camera.turnTo(SceneEntities.getNearest(bell));
					}
				}
			}
			
			timePassed = startTime - System.currentTimeMillis();
			if(timePassed % 15 == 0) {
            	//Time.sleep(700,1000);
            	System.out.println("XP Gained: " + (Skills.Skill.CONSTRUCTION.getExperience() - initialExp) + ", XP to level: " + (Skills.Skill.CONSTRUCTION.getExperienceToNextLevel()) + ", XP/hr: " + (((long)(Skills.Skill.CONSTRUCTION.getExperience() - initialExp)) * 3600000) / (System.currentTimeMillis() - startTime) + ", Runtime: " + (((System.currentTimeMillis() - startTime) / 1000) / 3600) + ":" + ((((System.currentTimeMillis() - startTime) / 1000) % 3600) / 60) + ":" + (((System.currentTimeMillis() - startTime) / 1000) % 60));
	            System.out.println("Made " + ((Skills.Skill.CONSTRUCTION.getExperience() - initialExp) / 480) + " larders");
	            if(timePassed % 2000 == 0) {
	            	System.out.println("Something unlikely just happened!");
	                Tabs.STATS.open();
	    			Mouse.moveRandomly(300);
	    			Time.sleep(1000,2500);
	    			//Tabs.CLAN_CHAT.open();
	            }
	        }
        }
		
		@Override
        public boolean shouldExecute() {

			if (Players.getLocal() != null && Inventory.getCount() > 0) {
				return true;
			}

			return false;
        }
	}
	public class Antiban extends Node implements Task {

		@Override
		public void run() {
			
			if(Inventory.getItem(longbow).getStackSize() % 10 == 0) {
				checkStats();
			}
			else if(Inventory.getItem(longbow).getStackSize() % 2 == 0) {
				Camera.setAngle((int)(1 + (Math.random()) * 358));
				System.out.println("Changing angle...");
			}
			else {
				Camera.setPitch((int)(32 + (Math.random()) * 65));
				System.out.println("Changing pitch...");
			}
		}

		@Override
		public boolean shouldExecute() {

			if(System.currentTimeMillis() % (100 - ran) == 0 && Players.getLocal()) {
				System.out.println("Running antiban...");
				return true;
			}

			return false;
		}
	}
}