package PurplePapaya.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PurplePapaya.dto.VoteDto;
import PurplePapaya.exeption.PurplePapayaException;
import PurplePapaya.service.VoteService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/vote")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Object> vote(@RequestBody VoteDto voteDto) throws PurplePapayaException {
        voteService.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}