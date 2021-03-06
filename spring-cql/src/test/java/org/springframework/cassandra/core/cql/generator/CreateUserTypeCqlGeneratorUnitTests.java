/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cassandra.core.cql.generator;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.cassandra.core.cql.generator.CreateUserTypeCqlGenerator.*;

import org.junit.Test;
import org.springframework.cassandra.core.keyspace.CreateUserTypeSpecification;

import com.datastax.driver.core.DataType;

/**
 * Unit tests for {@link CreateUserTypeCqlGenerator}.
 * 
 * @author Mark Paluch
 */
public class CreateUserTypeCqlGeneratorUnitTests {

	@Test // DATACASS-172
	public void createUserType() {

		CreateUserTypeSpecification spec = CreateUserTypeSpecification //
				.createType("address") //
				.field("city", DataType.varchar());

		assertThat(toCql(spec)).isEqualTo("CREATE TYPE address (city varchar);");
	}

	@Test // DATACASS-172
	public void createMultiFieldUserType() {

		CreateUserTypeSpecification spec = CreateUserTypeSpecification //
				.createType("address") //
				.field("zip", DataType.ascii()) //
				.field("city", DataType.varchar());

		assertThat(toCql(spec)).isEqualTo("CREATE TYPE address (zip ascii, city varchar);");
	}

	@Test // DATACASS-172
	public void createUserTypeIfNotExists() {

		CreateUserTypeSpecification spec = CreateUserTypeSpecification //
				.createType() //
				.name("address").ifNotExists().field("zip", DataType.ascii()) //
				.field("city", DataType.varchar());

		assertThat(toCql(spec)).isEqualTo("CREATE TYPE IF NOT EXISTS address (zip ascii, city varchar);");
	}

	@Test(expected = IllegalArgumentException.class) // DATACASS-172
	public void generationFailsIfNameIsNotSet() {
		toCql(CreateUserTypeSpecification.createType());
	}

	@Test(expected = IllegalArgumentException.class) // DATACASS-172
	public void generationFailsWithoutFields() {
		toCql(CreateUserTypeSpecification.createType().name("hello"));
	}
}
