/**
 * Copyright (C) 2014 Thiago Moreira (tmoreira2020@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.thiagomoreira.replicon.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	public static Date translateDate(
			br.com.thiagomoreira.replicon.model.Date date) {
		return translateDate(date, false);
	}

	public static Date translateDate(
			br.com.thiagomoreira.replicon.model.Date date, boolean isEndDate) {

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-0"));

		calendar.set(Calendar.YEAR, date.getYear());
		calendar.set(Calendar.MONTH, date.getMonth() - 1);
		if (isEndDate) {
			calendar.set(Calendar.DAY_OF_MONTH, date.getDay() + 1);
		} else {
			calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
		}
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.setTimeZone(TimeZone.getTimeZone("GMT-0"));

		return calendar.getTime();
	}

	public static br.com.thiagomoreira.replicon.model.Date translateDate(
			Date date) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTimeZone(TimeZone.getTimeZone("GMT-0"));
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);

		calendar.setTime(date);

		br.com.thiagomoreira.replicon.model.Date result = new br.com.thiagomoreira.replicon.model.Date();

		result.setYear(calendar.get(Calendar.YEAR));
		result.setMonth(calendar.get(Calendar.MONTH) + 1);
		result.setDay(calendar.get(Calendar.DAY_OF_MONTH));

		return result;
	}

	public static br.com.thiagomoreira.replicon.model.Timestamp translateTimestamp(
			Date date, java.util.TimeZone tz) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(tz);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.setTime(date);

		br.com.thiagomoreira.replicon.model.Timestamp result = new br.com.thiagomoreira.replicon.model.Timestamp();

		result.setYear(calendar.get(Calendar.YEAR));
		result.setMonth(calendar.get(Calendar.MONTH) + 1);
		result.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		result.setHour(calendar.get(Calendar.HOUR_OF_DAY));
		result.setMinute(calendar.get(Calendar.MINUTE));
		result.setSecond(calendar.get(Calendar.SECOND));

		return result;
	}
}
