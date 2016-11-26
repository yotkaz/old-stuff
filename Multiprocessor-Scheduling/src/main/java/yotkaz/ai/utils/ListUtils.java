package yotkaz.ai.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author yotkaz
 *
 */
public class ListUtils {

	private static Random random = new Random();
	
	public static <T> List<T> getRandomSublist(List<T> list) {
		return getRandomSublist(list, 0);
	}

	public static <T> List<T> getRandomSublist(List<T> list, int maxCount) {
		List<T> available = new ArrayList<T>(list);
		List<T> result = new LinkedList<>();
		int randomCount = random.nextInt((maxCount > 0 && maxCount < available.size()) ? maxCount : available.size());
		for (int i = 0; i < randomCount; i++) {
			int randomIndex = random.nextInt(available.size());
			T randomObject = available.get(randomIndex);
			available.remove(randomIndex);
			result.add(randomObject);
		}
		return result;
	}
}
