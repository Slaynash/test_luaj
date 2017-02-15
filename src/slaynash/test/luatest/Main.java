package slaynash.test.luatest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
	
	private static int createdEntities;
	private static int livingEntities;
	public static List<Entity> entities = new ArrayList<Entity>();
	
	public static void main(String[] args) throws InterruptedException{
		Random r = new Random();
		int i = 0;
		while(createdEntities < 10 || livingEntities != 0){
			i = r.nextInt(20);
			if(i == 0){
				for(Entity e:entities) if(!e.isDead()) {
					e.die();
					livingEntities--;
					break;
				}
			}
			else if(i == 19 && createdEntities < 10){
				Entity e = new Entity();
				e.born();
				entities.add(e);
				createdEntities++;
				livingEntities++;
			}
			for(Entity e:entities) if(!e.isDead()) e.live();
			Thread.sleep(10);
		}
	}
	
	/*
	ordre:
	Entity e = new Entity();
	e.born();
	e.live();
	e.die();
	*/
}
