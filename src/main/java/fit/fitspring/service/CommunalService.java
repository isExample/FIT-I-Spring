package fit.fitspring.service;

import fit.fitspring.controller.dto.communal.AnnouncementDto;
import fit.fitspring.controller.dto.communal.MyPageDto;
import fit.fitspring.controller.dto.communal.ReviewDto;
import fit.fitspring.controller.dto.communal.TermDto;
import fit.fitspring.controller.dto.communal.TrainerInformationDto;
import fit.fitspring.controller.dto.customer.WishDto;
import fit.fitspring.domain.account.*;
import fit.fitspring.domain.matching.WishList;
import fit.fitspring.domain.review.Review;
import fit.fitspring.domain.trainer.*;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fit.fitspring.exception.common.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommunalService {

    private final AnnouncementRepository announcementRepository;
    private final TermRepository termRepository;
    private final TrainerRepository trainerRepository;
    private final UserImgRepository userImgRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public List<AnnouncementDto> getAnnouncementList() {
        List<Announcement> annoList = announcementRepository.findAll();
        List<AnnouncementDto> annoDtoList = new ArrayList<>();
        for(Announcement i : annoList){
            AnnouncementDto annoDto = new AnnouncementDto(i.getTitle(), i.getContent(), i.getModifiedDate().toLocalDate());
            annoDtoList.add(annoDto);
        }
        return annoDtoList;
    }
    @Transactional
    public List<TermDto> getTermList(){
        List<Term> termList = termRepository.findAll();
        List<TermDto> termDtoList = new ArrayList<>();
        for(Term i : termList){
            TermDto termDto = new TermDto(i.getId(), i.getName(), i.getDetail());
            termDtoList.add(termDto);
        }
        return termDtoList;
    }

    @Transactional
    public List<ReviewDto> getTrainerReviewList(Long trainerIdx){
        Optional<Trainer> optional = trainerRepository.findById(trainerIdx);
        if(optional.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_TRAINERIDX);
        }
        List<Review> reviewList = optional.get().getReviewList();
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        for(Review i : reviewList){
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setName(i.getCustomer().getName());
            reviewDto.setProfile(i.getCustomer().getProfile());
            reviewDto.setGrade(i.getGrade());
            reviewDto.setContents(i.getContent());
            reviewDto.setCreatedAt(i.getCreatedDate().toLocalDate());
            reviewDtoList.add(reviewDto);
        }
        return reviewDtoList;
    }

    @Transactional
    public TrainerInformationDto getTrainerInformation(Long trainerIdx){
        Optional<Trainer> optional = trainerRepository.findById(trainerIdx);
        if(optional.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_TRAINERIDX);
        }
        Optional<UserImg> userImg = userImgRepository.findByTrainer(optional.get());
        TrainerInformationDto trainerInfo = new TrainerInformationDto();
        trainerInfo.setName(optional.get().getUser().getName());
        trainerInfo.setProfile(userImg.get().getProfile());
        trainerInfo.setBackground(userImg.get().getBackGround());
        trainerInfo.setLevelName(optional.get().getLevel().getName());
        trainerInfo.setSchool(optional.get().getSchool());
        trainerInfo.setGrade(optional.get().getGrade());
        trainerInfo.setCost(optional.get().getPriceHour());
        trainerInfo.setIntro(optional.get().getIntro());
        trainerInfo.setService(optional.get().getService());
        List<ReviewDto> reviewList = getTrainerReviewList(trainerIdx);
        if(reviewList.toArray().length > 3){
            reviewList = reviewList.subList(0, 3);
        }
        trainerInfo.setReviewDto(reviewList);
        List<EtcImg> etcImgList = userImg.get().getEtcImgList();
        List<String> imageList = new ArrayList<>();
        for(EtcImg i : etcImgList){
            imageList.add(i.getEtcImg());
        }
        trainerInfo.setImageList(imageList);
        trainerInfo.setMatching_state(optional.get().getUser().getUserState().equals("A"));
        trainerInfo.setCategory(convertCategoryForClient(optional.get().getCategory()));
        return trainerInfo;
    }

    @Transactional
    public MyPageDto getMyPageBriefInformation() throws BusinessException{
        Optional<Account> optional = accountRepository.findById(SecurityUtil.getLoginUserId());
        if(optional.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }
        MyPageDto myPageDto = new MyPageDto();
        if(optional.get().getAccountType().equals(AccountType.TRAINER)){
            Optional<Trainer> optionalT = trainerRepository.findById(optional.get().getId());
            if(optionalT.isEmpty()){
                throw new BusinessException(ErrorCode.INVALID_TRAINERIDX);
            }
            myPageDto.setProfile(optionalT.get().getUserImg().getProfile());
        }
        else{
            myPageDto.setProfile(optional.get().getProfile());
        }
        myPageDto.setUserIdx(optional.get().getId());
        myPageDto.setUserName(optional.get().getName());
        myPageDto.setEmail(optional.get().getEmail());
        myPageDto.setLocation(optional.get().getLocation());
        return myPageDto;
    }

    public String convertCategoryForClient(Category category){
        if (category.equals(Category.PERSONAL_PT)){
            return "pt";
        } else if(category.equals(Category.DIET)){
            return "diet";
        } else if(category.equals(Category.FOOD_CHECK)){
            return "food";
        } else if(category.equals(Category.REHAB)){
            return "rehab";
        } else if(category.equals(Category.FIT_MATE)){
            return "friend";
        }
        return null;
    }
}