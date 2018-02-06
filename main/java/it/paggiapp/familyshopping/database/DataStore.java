package it.paggiapp.familyshopping.database;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import it.paggiapp.familyshopping.database.FamilyDatabase;

public final class DataStore {
    private DataStore() {}
    private static final Executor EXEC = Executors.newSingleThreadExecutor();

    private static FamilyDatabase db;

    public static void init(Context context) {
        db = new FamilyDatabase(context);
    }

    public static FamilyDatabase getDB() {
        return db;
    }

    public static void execute(Runnable runnable) {
        EXEC.execute(runnable);
    }
}
