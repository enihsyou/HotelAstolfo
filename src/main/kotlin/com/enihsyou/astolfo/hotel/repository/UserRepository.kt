package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.User
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.PathVariable

@Repository("用户仓库")
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
interface UserRepository : PagingAndSortingRepository<User, Int> {

    fun findByPhoneNumber(@PathVariable(name = "phone_number") phone_number: String): User?

    fun findByNickname(@PathVariable(name = "nickname") nickname: String): User?

    fun findByRole(@PathVariable(name = "role") role: String): User?

}


