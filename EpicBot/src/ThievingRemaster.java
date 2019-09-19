import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import com.epicbot.api.ActiveScript;
import com.epicbot.api.GameType;
import com.epicbot.api.Manifest;
import com.epicbot.api.concurrent.Task;
import com.epicbot.api.concurrent.node.Node;
import com.epicbot.api.input.Keyboard;
import com.epicbot.api.input.Mouse;
import com.epicbot.api.os.methods.Combat;
import com.epicbot.api.os.methods.Game;
import com.epicbot.api.os.methods.Tabs;
import com.epicbot.api.os.methods.Calculations;
import com.epicbot.api.os.methods.Walking;
import com.epicbot.api.os.methods.Widgets;
import com.epicbot.api.os.methods.interactive.NPCs;
import com.epicbot.api.os.methods.interactive.Players;
import com.epicbot.api.os.methods.node.SceneEntities;
import com.epicbot.api.os.methods.tab.Equipment;
import com.epicbot.api.os.methods.tab.Skills;
import com.epicbot.api.os.methods.tab.inventory.Inventory;
import com.epicbot.api.os.methods.widget.Bank;
import com.epicbot.api.os.methods.widget.Camera;
import com.epicbot.api.os.wrappers.Tile;
import com.epicbot.api.os.wrappers.interactive.NPC;
import com.epicbot.api.os.wrappers.node.Item;
import com.epicbot.api.os.wrappers.node.SceneObject;
import com.epicbot.api.util.Filter;
import com.epicbot.api.util.Time;

@Manifest(author = "Luke", game = GameType.OldSchool, name = "ThievingRemaster", description = "Thieves master farmers, eats when low health, walks to bank and collects food/dodgy necklace(if broken), fastdrops inexpensive seeds between game ticks")

public class ThievingRemaster extends ActiveScript {
	@Override
	public boolean onStart() {
		Mouse.setSpeed(4150);
		
		provide(new Grab());
		provide(new Antiban());
		provide(new BankTask());
		
		return true;
	}
	
	@Override
	public void onStop() {
		Game.logout();
	}
	
	private final int initialExp = Skills.Skill.THIEVING.getExperience();
	private final long startTime = System.currentTimeMillis();
	Tile escapeTile = new Tile(3096,3248); // Safe area unreachable by guards
	Tile bankSquare = new Tile(3092,3245);
	Tile stealArea = new Tile(3079,3250);
	
	Item[] loot;
	int stealTotal, runningTotal, banking, timer = 0, localTimer = 0, wait = 0;

	int currentThieving = 0, afterThieving = 0, timeout = 0;

	Iterator<Item> itr;
	Robot r;
	
	NPC n;
	Item currentItem;

	int ran = 0;
	
	// Changes random factor to avoid detectable repetitious behaviour
	// Runs once per iteration
	public int changeRandom() {
		ran = (int)(30 + (Math.random()) * 30);
		return ran;
	}
	public class Grab extends Node implements Task {
		
		private final Filter<NPC> targets = new Filter<NPC>() {
			
			private int[] ids = new int[] { 3100, 3257 }; 
            public boolean accept(NPC target) {

                int i = target.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};
		
		private final Filter<NPC> guard = new Filter<NPC>() {
			private int[] ids = new int[] { 3259, 3260 }; 
            public boolean accept(NPC guard) {

                int i = guard.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};
		
		private final Filter<Item> necklace = new Filter<Item>() {
			private int[] ids = new int[] { 21143 }; //21144 noted
            public boolean accept(Item wearable) {

                int i = wearable.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};
		
		private final Filter<Item> food = new Filter<Item>() {
			private int[] ids = new int[] { 361, 329 }; // Default: Monkfish, lobster
            public boolean accept(Item food) {

                int i = food.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};
		
		private final Filter<Item> worthless = new Filter<Item>() {
			private int[] ids = new int[] { 1993,1935,5306,5292,5098,5291,5096,5101,5308,5324,5309,5305,5307,5097,5102,5104,5322,5318,5310,5319 }; 
            public boolean accept(Item seed) {

                int i = seed.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};
		
		@Override
		public void run() {
			ran = changeRandom();
			
			currentThieving = Skills.Skill.THIEVING.getExperience();
			
			try {
				r = new Robot();
			}
			catch (Exception e) {
				System.out.println("Error creating robot: " + e);
			}
			
			NPC targ = NPCs.getNearest(targets);
			if(!Walking.isRunEnabled() & Walking.getEnergy() > ran) {
				Walking.setRun(true);
			}
			
			if(Inventory.contains(necklace) && !Bank.isOpen() && !Equipment.contains(necklace)) {
				Inventory.getItem(necklace).interact("Wear");
			}
			
			if(NPCs.getNearest(guard) != null)
				n = NPCs.getNearest(guard);
			
			if(Players.getLocal().getAnimation() == 423 || (n != null && n.isInteractingWithLocalPlayer())) {
				Walking.setRun(true);
				while(Calculations.distanceTo(escapeTile) > 2) {
					Walking.walkPathMM(Walking.findPath(escapeTile));
					Time.sleep(1000);
				}
				Time.sleep(400,600);
				Walking.walkPathMM(Walking.findPath(stealArea));
			
			}
			if(Inventory.isFull()) {
				Inventory.getItem(food).click(true);
			}
				
				
			if(Combat.getLifePoints() > 3) {
				if(targ != null && !targ.isOnScreen()) {
					if(Calculations.distanceTo(targ) < 7) {
		                Time.sleep(50);
		                log.info("Turning to target");
		                Camera.turnTo(targ);
		            } 
		            else {
		            	Time.sleep(50);
		                Walking.walkTo(targ.getLocation());
		            }
		        }
				else if (Inventory.contains(food) && (targ == null || !targ.getLocation().isOnMap())){
					Walking.walkTileMM(stealArea);
				}
			}
			timer = 0;
			wait = 0;
			if(Players.getLocal().getAnimation() == 424) {
				if(Combat.getLifePoints() > 20) {
					itr = Inventory.getItems(worthless).iterator();
					while(itr.hasNext() && Combat.getLifePoints() > 10 && timer < 5) {
						Time.sleep(50,200);
						r.keyPress('q');
						itr.next().interact("Drop");
						timer++;
					}
					wait = 2500-(timer*374);
				}
				else {
					Time.sleep(200);
					Inventory.getItem(food).click(true);
					wait = 2000;
				}
					
				Time.sleep(wait,wait+50);
			}
				
	            
	        if(targ != null && targ.isOnScreen() && !Bank.isOpen() && Combat.getLifePoints() > 3) {
	            targ.interact("Pickpocket");
	            Time.sleep(1,100);
	            timeout = 0;
	            if(Combat.getLifePoints() > 3)
	            	targ.interact("Pickpocket");
	                
	            if(Inventory.isItemSelected())
	            	targ.click(true);
	                
	        }
	        else if(targ != null && Bank.isOpen()) 
	        	Walking.walkPathMM(Walking.findPath(targ.getLocation()));
	        
	        else if(targ == null) {
	            Walking.walkPathMM(Walking.findPath(stealArea));
	            Time.sleep(800);
	        }
	        
	        afterThieving = Skills.Skill.THIEVING.getExperience();
	        if(currentThieving == afterThieving) {
	        	timeout++;
	        	if(timeout > 50)
					Inventory.getItem(food).click(true);
	        }
            
        }
		

		
		@Override
        public boolean shouldExecute() {

			if (Players.getLocal() != null && Inventory.contains(food)) {
				
				return true;	
			}
			
            log.info("Banking");
            banking = 0;
            
			return false;
        }
	}
	
	public class Antiban extends Node implements Task {

		@Override
		public void run() {
			

			Tabs.CLAN_CHAT.open();

			Time.sleep(500,1000);
			Tabs.STATS.open();
			Time.sleep(2200, 3100);
			
		}
		
		@Override
		public boolean shouldExecute() {
			if(!Inventory.isFull() && Players.getLocal().getAnimation() == 424 && (System.currentTimeMillis() - startTime) % (20 + ran) == 0 && Combat.getLifePoints() > 3) 
				return true;

			return false;
		}
	
	}
	
	public class BankTask extends Node implements Task {
		
		private final Filter<SceneObject> filter1 = new Filter<SceneObject>() {
			private int[] ids = new int[] { 24101, 6943 };
            public boolean accept(SceneObject bankBooth) {

                int i = bankBooth.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};
		
		private final Filter<Item> food = new Filter<Item>() {
			private int[] ids = new int[] { 329, 361 }; 
            public boolean accept(Item target) {

                int i = target.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};
		
		private final Filter<Item> necklace = new Filter<Item>() {
			private int[] ids = new int[] { 21143 }; 
            public boolean accept(Item target) {

                int i = target.getID();
                for(int id : ids) {
                    if(i == id)
                        return true;
                }
                
                return false;
            }
		};

		public void run() {
			banking = 0;
			timer = 0;
			while(Calculations.distanceTo(bankSquare) > 5 && timer < 5) {
				Walking.walkPathMM(Walking.findPath(bankSquare));
				Time.sleep(500);
				timer++;
				if(timer == 4) 
					Camera.setAngle(Camera.getAngle() + 37);
			}
			
			if(bankSquare.isOnScreen()) {
				SceneObject bankBooth = SceneEntities.getNearest(filter1);
				if(!bankBooth.isOnScreen()) {
	                if(Calculations.distanceTo(bankBooth) <= 8) {
	                    log.info("Can't find bank booth - turning to find one");
	                    Camera.turnTo(bankBooth);
	                    Time.sleep(50);
	                } 
	                else {
	                    log.info("Walking to: " + bankBooth.getLocation());
	                    Walking.walkTo(bankBooth);
	                    Time.sleep(50);
	                }
	            } 
				else {
	                timer = 0;
	                if(Widgets.get(402).getChild(2).getChild(11) != null) {
	                	Widgets.get(402).getChild(2).getChild(11).click(true);
	                }
	                if(!Bank.isOpen() && Players.getLocal().isMoving())  
	                	bankBooth.interact("Bank");
	                	
	                
	                while(!Bank.isOpen() && timer < 5 && !Players.getLocal().isMoving()) {
	                	bankBooth.interact("Bank");
	                	Time.sleep(500 + timer*200);
	                	timer++;
	                }
	                
	                timer = 0;
	                while(Inventory.getCount() != 0 && Bank.isOpen() && timer < 10) {
	                	Bank.depositAll();
	                	timer++;
	                	Time.sleep(300);
	                }
	                if(Inventory.getCount() == 0 && banking == 0) {
	                	banking++;
	                	runningTotal += stealTotal;
	                	log.info("XP Gained: " + (Skills.Skill.THIEVING.getExperience() - initialExp) + ", XP to level: " + (Skills.Skill.THIEVING.getExperienceToNextLevel()) + ", XP/hr: " + (((long)(Skills.Skill.THIEVING.getExperience() - initialExp)) * 3600000) / (System.currentTimeMillis() - startTime) + ", Runtime: " + (((System.currentTimeMillis() - startTime) / 1000) / 3600) + ":" + ((((System.currentTimeMillis() - startTime) / 1000) % 3600) / 60) + ":" + (((System.currentTimeMillis() - startTime) / 1000) % 60));
	                    log.info("Pickpocketed " + ((Skills.Skill.THIEVING.getExperience() - initialExp) / 43) + " times");
	                    log.info("We just stole " + stealTotal + " for a total of " + runningTotal);
	                }
	                
	                timer = 0;
	                while(!Inventory.contains(food) && Bank.isOpen() && timer < 10) {// && banking == 0) {
	                	Time.sleep(100);
	                	if(Bank.getItem(361) != null) {
	                		Bank.getItem(361).interact("Withdraw-10");
	                		Time.sleep(750);
	                	}
	                	else
	                		Bank.withdraw(361,10);
	                	Time.sleep(50);
	                	timer++;
	                	
	                	if(!Equipment.contains(necklace)) {
	                		localTimer = 0;
	                		while(!Inventory.contains(necklace) && Bank.isOpen() && localTimer < 10) {
	                			if(Bank.getItem(21143) != null) {
	                				Bank.getItem(21143).click(true);
	                				Time.sleep(750);
	                			}
	                			else
	    	                		Bank.withdraw(21143,1);
	                			Time.sleep(50);
	                			localTimer++;
	                		}
	                	}
	                	
	                	
	                }
	                Time.sleep(180,250);
	                
	            }	
			
			}
			
		}
		@Override
		public boolean shouldExecute() {
			if (Players.getLocal() != null && !Inventory.contains(food)) 
				return true;
			
			return false;
		}
    
	}

	
}