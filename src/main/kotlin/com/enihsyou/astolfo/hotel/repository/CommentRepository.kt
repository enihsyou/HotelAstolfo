package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Comment
import org.springframework.data.repository.PagingAndSortingRepository
import org.yaml.snakeyaml.events.Event

interface CommentRepository:PagingAndSortingRepository<Comment, Int> {
}
