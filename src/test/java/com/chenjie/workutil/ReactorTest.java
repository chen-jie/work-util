package com.chenjie.workutil;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactorTest {

	@Test
	public void findAll() {
		Flux.just("Hello", "World").subscribe(System.out::println);

		List<String> list = new ArrayList<>();
		Flux.generate(sink -> {
			sink.next("Hello");
			sink.complete();
		}).subscribe(System.out::println);

	}

	@Test
	public void testFlatMap()
	{
		List<Integer> together = Stream.of(Arrays.asList(1, 2), Arrays.asList(3, 4))
				.flatMap(numbers -> numbers.stream())
				.collect(toList());
		System.out.println(together);
		together.stream().map(top -> top*2).forEach(System.out::println);

		ArrayList list = new ArrayList();
		list.stream();
	}

	@Test
	public void testFunctionInterface()
	{
		Predicate<Integer> predOdd = a -> a % 2 == 1;
		System.out.println(predOdd.test(5));

		Consumer<Integer> a = System.out::println;
		a.accept(5555);
	}

	@Test
	public void testFlux()
	{
		Flux.generate(sink -> {
			sink.next("Hello");
			sink.complete();
		}).subscribe(System.out::println);


		final Random random = new Random();
		Flux.generate(ArrayList::new, (list, sink) -> {
			int value = random.nextInt(100);
			list.add(value);
			sink.next(value);
			if (list.size() == 10) {
				sink.complete();
			}
			return list;
		}).subscribe(System.out::println);
	}

	@Test
	public void testStream()
	{
		String[] words = {"hello","word","bye"};
		Arrays.stream(words).map(str -> str.split(""))
				.flatMap(arr -> Arrays.stream(arr))
				.collect(Collectors.toList()).forEach(System.out::println);
		System.out.println(Arrays.toString("HelloWolrd".split("")));
	}
}
