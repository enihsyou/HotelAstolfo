package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.RoomDirection
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@Repository("房间朝向仓库")
@RepositoryRestResource(path = "roomDirections")
interface RoomDirectionRepository : PagingAndSortingRepository<RoomDirection, Int> {

    fun findByType(type: String): RoomDirection?
}
