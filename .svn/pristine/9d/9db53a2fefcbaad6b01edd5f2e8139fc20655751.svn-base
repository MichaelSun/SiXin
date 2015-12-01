package com.core.json;



/**
 * @author dingwei.chen
 * */
public final  class JsonFormatPrinter {

	public static void printJsonFormat(JsonObject jsonObject){
		String[] keys = jsonObject.getKeys();
		StringBuilder sbuilder = new StringBuilder();
		for(String k:keys){
			sbuilder.append(parseJson(k, jsonObject,0)+"\n");
		}
	}
	private static final boolean flag = true;
	public static String parseJson(String key,JsonObject jsonObject,int emptyNumber){
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(getEmptyLength(emptyNumber));
		emptyNumber += 2;
		String line = "";
		while(flag){
			long number = jsonObject.getNum(key);//解析数字
			if(number != 0){
				line = number+"";	break;
			}
			String str = jsonObject.getString(key);
			if(str!=null){
				line = str;	break;
			}
			JsonArray array = jsonObject.getJsonArray(key);
			if(array!=null){
				JsonObject[] jobjectArray = new JsonObject[array.size()];
				array.copyInto(jobjectArray);
				for(JsonObject ob:jobjectArray){
					String[] keys = ob.getKeys();
					for(String k : keys){
						line = parseJson(k, ob, emptyNumber);
					}
				}break;
			}
			JsonObject o = jsonObject.getJsonObject(key);
			if(o!=null){
				line = parseJson(key, o, emptyNumber);	break;
			}
			break;
		}
		sbuilder.append(key+"="+line+"\n");
		return sbuilder.toString(); 
	}
	private static String getEmptyLength(int emptyNumber){
		StringBuilder  builder = new StringBuilder(emptyNumber+1);
		for(int k = 0;k<emptyNumber; k++){
			builder.append(" ");
		}
		return builder.toString();
	}
	
	
	
}
