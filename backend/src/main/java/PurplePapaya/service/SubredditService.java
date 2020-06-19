package PurplePapaya.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import PurplePapaya.dto.SubredditDto;
import PurplePapaya.exeption.PurplePapayaException;
import PurplePapaya.mapper.SubredditMapper;
import PurplePapaya.model.Subreddit;
import PurplePapaya.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }   

    @Transactional
    public List<SubredditDto> getAllSubreddits() {
        return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToDto).collect(Collectors.toList());
    }

	public SubredditDto getSubreddit(Long id) throws PurplePapayaException {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new PurplePapayaException("No subreddit found with ID - " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
        }
}