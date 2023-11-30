package com.myapp.warmwave.domain.favorite_inst.controller;

import com.myapp.warmwave.domain.favorite_inst.dto.FavoriteInstDto;
import com.myapp.warmwave.domain.favorite_inst.service.FavoriteInstService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/favorite")
public class FavoriteInstController {
    private final FavoriteInstService favoriteInstService;

    @PostMapping
    public ResponseEntity<Void> createFavorite(@PathVariable("userId") Long institutionId
//                                               , Authentication authentication
    ) {
//        String email = authentication.getName();
        favoriteInstService.createFavoriteInst(institutionId, "test01@gmail.com");
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteInstDto>> readAllMyFavorite(@PathVariable("userId") Long individualId) {
        return ResponseEntity.ok(favoriteInstService.findAllFavoriteInstByIndividual(individualId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFavorite(@PathVariable("userId") Long institutionId
//            , Authentication authentication
    ) {
//        String email = authentication.getName();
        favoriteInstService.deleteFavoriteInst(institutionId, "test01@gmail.com");
        return ResponseEntity.noContent().build();
    }
}
