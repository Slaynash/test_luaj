package slaynash.test.luatest;

import java.io.File;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

public class Entity {
	
	public Globals m_globals;
	
	private LuaValue program;  //programme principal (fichier)
	private LuaValue funcBorn; //fonction born()
	private LuaValue funcLive; //fonction live(age)
	private LuaValue funcDie;  //fonction die()
	
	
	private static long bornTime; //System.currentTimeMillis() au moment de l'appel de born()
	private boolean dead = false; //l'entité est-elle morte ?
	private int lifeTime = 0;	  //temps de vie (utilisé uniquement si l'entité est morte)

	
	public Entity(){
		m_globals = JsePlatform.standardGlobals();
		
		//création d'une 'library' nomée 'life' avec la fonction 'getLifeTime()'
		LuaValue life = new LuaTable();
		m_globals.set("life", life);
		life.set("getLifeTime", new lifeTime());
		
		//chargement/lancement du fichier baseEntity.lua dans cette instance
		try{
			File rom = new File("baseEntity.lua");
			program = m_globals.get("dofile").call( LuaValue.valueOf(rom.toString()));
		}
		catch (LuaError e){
			System.out.println("Failed to load baseEntity.lua");
			throw e;
		}
		
		//définition des fonctions lua pour un appel plus rapide
		funcBorn = m_globals.get("born");
		funcLive = m_globals.get("live");
		funcDie = m_globals.get("die");
	}
	
	public void born(){
		bornTime = System.currentTimeMillis();
		funcBorn.call();
	}
	
	public void live(){
		lifeTime = (int)(System.currentTimeMillis()-bornTime)/1000;
		funcLive.call(LuaValue.valueOf(lifeTime));
	}
	
	public void die(){
		dead = true;
		lifeTime = (int)(System.currentTimeMillis()-bornTime)/1000;
		funcDie.call();
	}
	
	
	
	
	public boolean isDead() {
		return dead;
	}
	
	
	
	
	public class lifeTime extends ZeroArgFunction {
		public LuaValue call() {
			return dead ? valueOf(lifeTime) : valueOf((int)(System.currentTimeMillis()-bornTime)/1000);
		}
	}
	
}
