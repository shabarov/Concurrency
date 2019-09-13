package ru.shabarov.concurrency.completablefuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //Simple create/complete
        CompletableFuture<String> cf = new CompletableFuture<>();
        cf.complete("completed");
        System.out.println(cf.get());
        System.out.println(cf.get());

        //Task sequential chain
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CompletableFuture<Void> result = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "One-";
        }, executorService).thenApply(one -> one + "Two-").thenApply(two -> two + "Three")
                .thenAccept(System.out::println)
                .thenRun(() -> System.out.println("What's it!"));

        result.join();
        executorService.shutdown();
        System.out.println("Done = " + result.isDone());

        // Task Compose
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> "Hello, ");

        CompletableFuture<String> helloMike = hello.thenCompose(helloWord ->
                CompletableFuture.supplyAsync(() -> helloWord + "Mike!"));

        System.out.println(helloMike.get());

        // Task Combine
        CompletableFuture<String> pastry = CompletableFuture.supplyAsync(() -> "thin pastry");
        CompletableFuture<String> souce = CompletableFuture.supplyAsync(() -> "cream souce");
        CompletableFuture<String> ingredients = CompletableFuture.supplyAsync(() -> "peperoni, tomatoes, cheese");
        CompletableFuture<String> pizza = pastry.thenCombine(souce, (p, s) -> "Pizza on " + p + ", souce - " + s).thenCombine(ingredients, (p, i) ->
                p + ", ingredients: " + i);
        System.out.println("Pizza is ready: " + pizza.get());

        // AllOf
        List<Integer> digits = Arrays.asList(1, 2, 3, 4, 5);
        List<CompletableFuture<Double>> poweredByTwo =
                digits.stream().map(d -> CompletableFuture.supplyAsync(() -> Math.pow(d, 2))).collect(Collectors.toList());

        CompletableFuture.allOf(poweredByTwo.toArray(new CompletableFuture[0]))
                .thenApply(v -> poweredByTwo.stream().map(CompletableFuture::join).collect(Collectors.toList()))
                .thenAccept(System.out::println);

        // AnyOf
        CompletableFuture<String> firstRunner = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "First runner";
        });
        CompletableFuture<String> secondRunner = CompletableFuture.supplyAsync(() -> "Second runner");
        CompletableFuture<String> thirdRunner = CompletableFuture.supplyAsync(() -> "Third runner");
        CompletableFuture.anyOf(thirdRunner, secondRunner, firstRunner).thenApply(o -> {
            System.out.println("The winner: " + o);
            return null;
        } );

        // Exception handling
        final String mailRegexp = "[^@]+@[^\\.]+\\..+";
        String email = "some.incorrect.email.com";
        CompletableFuture<String> emailSendResult = CompletableFuture.supplyAsync(() -> {
            if(!email.matches(mailRegexp)) {
                throw new IllegalArgumentException("Incorrect e-mail");
            }
            //send message...
            return "success";
        }).handle((res, ex) -> {
            if(ex != null) {
                System.out.println("Exception: " + ex.getMessage());
                return "Failure";
            }
            return res;
        });

        System.out.println("EMail send result : " + emailSendResult.get());

    }
}
