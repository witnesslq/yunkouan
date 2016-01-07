/**
 * Copyright (C) 2012-2014 Philip W. Sorst <philip@sorst.net>
 * and individual contributors as indicated
 * by the @authors tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yunkouan.lpid.persistence.entity;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public class LongIdEntity extends AbstractEntity<Long>{

	@Id
	private Long id;


	protected LongIdEntity(){
	}


	public LongIdEntity(final Long id){
		this.id = id;
	}


	@Override
	public Long getId(){
		return this.id;
	}

}
