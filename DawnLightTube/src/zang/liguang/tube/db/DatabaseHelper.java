package zang.liguang.tube.db;

import java.sql.SQLException;

import zang.liguang.tube.bean.YouTubeLanguageModle;
import zang.liguang.tube.bean.YouTubeRegionModle;
import zang.liguang.tube.bean.YouTubeVideoCatgoryModle;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{

	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "helloNoBase.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 3;

	// the DAO object we use to access the SimpleData table
	private Dao<YouTubeLanguageModle, Integer> YouTubeLanguageModleDao = null;
	private Dao<YouTubeRegionModle, Integer> YouTubeRegionModleDao = null;
	private Dao<YouTubeVideoCatgoryModle, Integer> YouTubeVideoCatgoryModleDao = null;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, YouTubeLanguageModle.class);
			TableUtils.createTable(connectionSource, YouTubeRegionModle.class);
			TableUtils.createTable(connectionSource, YouTubeVideoCatgoryModle.class);

			// here we try inserting data in the on-create as a test
			// Dao<SimpleData, Integer> dao = getSimpleDataDao();
			// long millis = System.currentTimeMillis();
			// // create some entries in the onCreate
			// SimpleData simple = new SimpleData(millis);
			// dao.create(simple);
			// simple = new SimpleData(millis + 1);
			// dao.create(simple);
			// Log.i(DatabaseHelper.class.getName(),
			// "created new entries in onCreate: " + millis);
		}
		catch (SQLException e)
		{
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		try
		{
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, YouTubeLanguageModle.class, true);
			TableUtils.dropTable(connectionSource, YouTubeRegionModle.class, true);
			TableUtils.dropTable(connectionSource, YouTubeVideoCatgoryModle.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		}
		catch (SQLException e)
		{
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It
	 * will create it or just give the cached value.
	 */
	public Dao<YouTubeLanguageModle, Integer> getYouTubeLanguageModleDao() throws SQLException
	{
		if (YouTubeLanguageModleDao == null)
		{
			YouTubeLanguageModleDao = getDao(YouTubeLanguageModle.class);
		}
		return YouTubeLanguageModleDao;
	}
	public Dao<YouTubeRegionModle, Integer> getYouTubeRegionModleDao() throws SQLException
	{
		if (YouTubeRegionModleDao == null)
		{
			YouTubeRegionModleDao = getDao(YouTubeRegionModle.class);
		}
		return YouTubeRegionModleDao;
	}
	public Dao<YouTubeVideoCatgoryModle, Integer> getYouTubeVideoCatgoryModleDao() throws SQLException
	{
		if (YouTubeVideoCatgoryModleDao == null)
		{
			YouTubeVideoCatgoryModleDao = getDao(YouTubeVideoCatgoryModle.class);
		}
		return YouTubeVideoCatgoryModleDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close()
	{
		super.close();
		YouTubeLanguageModleDao = null;
		YouTubeVideoCatgoryModleDao = null;
		YouTubeRegionModleDao = null;
	}
}
