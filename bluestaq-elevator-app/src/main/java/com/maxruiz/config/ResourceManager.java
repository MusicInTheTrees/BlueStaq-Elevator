package com.maxruiz.config;

/* 
 * Known incomplete class. I failed to get Gson to work.
 * I followed guides online.
 * 1) You will find in my repo that there is a 'scripts' folder that has
 *     how I attempted to manually install the module after downloading it
 * 2) You will also find in the 'libs' folder the .jar file that I used
 * 3) You will see in my pom file that I really tried (several) methods to get
 *     it to work.
 * ----------------------------------------------
 * PLEASE SEE MY NOTES IN THE FILE: notes.txt
 * ----------------------------------------------
 */



public class ResourceManager
{

}

// import com.google.gson.Gson;
// import com.google.gson.JsonObject;

// import java.io.FileReader;
// import java.io.IOException;

// public class ResourceManager 
// {
//   // Initial resource json file that contains other resource information
//   private String RESOURCE_DB_PATH = "resources/resource_db.json";

//   // Keys that index info in the json db
//   private String KEY_JSON_PATHS = "json_paths";

//   // JSON object
//   JsonObject m_jsonObject = null;

//   // Singleton
//   private static ResourceManager self = null;

//   private ResourceManager()
//   {
//     Gson gson = new Gson();

//     //Try to load the resource database
//     try 
//     {
//       FileReader reader = new FileReader(RESOURCE_DB_PATH);
//       m_jsonObject = gson.fromJson(reader, JsonObject.class);
//       System.out.println(m_jsonObject);
//     }
//     catch (IOException e)
//     {
//       e.printStackTrace();
//       m_jsonObject = new JsonObject();
//     }
//   }

//   public static ResourceManager get()
//   {
//     if (null == self)
//     {
//       self = new ResourceManager();
//     }

//     return self;
//   }

  
  
// }
