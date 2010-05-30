package j2DbParser.hooks;

import j2DbParser.db.SqlColumn;
import j2DbParser.guice.Guicer;
import j2DbParser.system.logging.LogFactory;
import j2DbParser.utils.IterableDecorator;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.NotImplementedException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public enum HookRunner {
	POST_CREATE_TABLE(IPostCreateTable.class) {
		@Override
		public void runCreate(String table, Set<SqlColumn> columns,
				String createQuery) {
			for (IHook hook : hookList) {
				if (hook instanceof IPostCreateTable) {
					IPostCreateTable pct = (IPostCreateTable) hook;
					pct.runCreate(table, columns, createQuery);
				}
			}
		}
	},
	POST_INSERT(IPostInsert.class) {
		@Override
		public boolean runInsert(String table, Map<String, String> map) {
			for (IHook hook : hookList) {
				if (hook instanceof IPostInsert) {
					IPostInsert pi = (IPostInsert) hook;
					if (pi.runInsert(table, map)) {
						return true;
					}
				}
			}
			return false;
		}
	},
	POST_CREATE_TABLES(IPostCreateTables.class) {
		@Override
		public void run() {
			for (IHook hook : hookList) {
				if (hook instanceof IPostCreateTables) {
					IPostCreateTables pi = (IPostCreateTables) hook;
					pi.run();
				}
			}
		}
	},
	POST_INSERTS(IPostInserts.class) {
		@Override
		public void run() {
			for (IHook hook : hookList) {
				if (hook instanceof IPostInserts) {
					IPostInserts pi = (IPostInserts) hook;
					pi.run();
				}
			}
		}
	};

	protected static final Logger log = LogFactory.getLogger(HookRunner.class);
	protected final Class<? extends IHook> clazz;

	protected static ImmutableList<IHook> hookList = findClasses();

	private HookRunner(Class<? extends IHook> clazz) {
		this.clazz = clazz;
	}

	private static ImmutableList<IHook> findClasses() {
		Iterable<IHook> it = IterableDecorator.create(ServiceLoader.load(
				IHook.class).iterator());
		Builder<IHook> builder = ImmutableList.builder();
		for (IHook hok : it) {
			hok = Guicer.getInstance(hok.getClass());
			builder.add(hok);
		}
		return builder.build();
	}

	public void run() {
		throw new NotImplementedException(name());
	}

	public boolean runInsert(String table, Map<String, String> map) {
		throw new NotImplementedException(name());
	}

	public void runCreate(String table, Set<SqlColumn> columns,
			String createQuery) {
		throw new NotImplementedException(name());
	}
}
