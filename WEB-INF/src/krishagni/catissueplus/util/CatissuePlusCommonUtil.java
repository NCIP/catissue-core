
package krishagni.catissueplus.util;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

public class CatissuePlusCommonUtil
{

	public static Gson getGson()
	{
		JsonDeserializer dese = new JsonDeserializer<Date>()
		{

			DateFormat df = new SimpleDateFormat(ApplicationProperties.getValue("date.pattern"));

			@Override
			public Date deserialize(final JsonElement json, final Type typeOfT,
					final JsonDeserializationContext context) throws JsonParseException
			{
				Date retObj = null;
				try
				{

					if (!Validator.isEmpty(json.getAsString()))
					{
						retObj = df.parse(json.getAsString());
					}

				}
				catch (ParseException e)
				{
					throw new JsonParseException("date");
				}
				return retObj;
			}
		};
		GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Date.class, dese);
		Gson gson = gsonBuilder.setDateFormat(ApplicationProperties.getValue("date.pattern"))
				.create();
		return gson;

	}

}
