package com.example.rewrite.controller;

import com.example.rewrite.entity.Address;
import com.example.rewrite.repository.address.AddressRepository;
import com.example.rewrite.service.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/detail") //주소지 페이지
    public String addressDetail(/*@RequestParam("addressId") String addressId,*/ Model model) {

        List<Address> list = addressService.getAddress("test");
        List<Map<String, Object>> address = new ArrayList<>(); //주소지 분리

        for(Address List : list){
            Map<String, Object> map = new HashMap<>();

            //기본주소지 문자열로 변경
            List.setIsDefault("C".equals(List.getIsDefault()) ? "기본주소지" : "");
            map.put("entity", List);

            //주소지 분리
            String[] addressParts = List.getAddress().split("/");
            map.put("postcode", addressParts[0]);
            map.put("addr", addressParts[1]);
            map.put("detailAddress", addressParts[2]);
            address.add(map);
        }


        model.addAttribute("list", address);

        return "address/addressDetail";
    }

    @GetMapping("/reg") //주소지 등록 페이지
    public String addressReg(){

        return "address/addressWrite";
    }


    @PostMapping("/write") //주소지 추가

    public String addressWrite(Address address,
                               @RequestParam("postcode")String postcode,
                               @RequestParam("addr")String addr,
                               @RequestParam("detailAddress")String addressDetail,
                               @RequestParam("phone1")String phone1,
                               @RequestParam("phone2")String phone2,
                               @RequestParam("phone3")String phone3
                               ){


        address.setUid("test"); //세션으로 변경 예정

        address.setIsDefault("N"); //기본값 설정
        address.setDelChk("N"); //기본값 설정

        //분리된 주소, 전화번호 합성 후 DB에 저장
        address.setAddress(postcode+"/"+addr+"/"+addressDetail);
        address.setPhoneNum(phone1 + "-" + phone2 +"-"+ phone3);


        addressService.addressWrite(address);

        return "redirect:/address/detail";
    }


    @PostMapping("/edit") //주소지 수정 페이지
    public String addressEdit(@RequestParam("addressId")Long addressId, Model model){

        Address entity = addressService.updateAddress(addressId);

        //주소지 분리
        String[] addressParts = entity.getAddress().split("/");
        model.addAttribute("postCode",addressParts[0]);
        model.addAttribute("addr",addressParts[1]);
        model.addAttribute("detailAddress",addressParts[2]);

        //전화번호 분리
        String[] phoneParts = entity.getPhoneNum().split("-");
        model.addAttribute("phone1",phoneParts[0]);
        model.addAttribute("phone2",phoneParts[1]);
        model.addAttribute("phone3",phoneParts[2]);

        return "address/addressWrite";
    }

    @PostMapping("/default") //기본주소지 선택
    public String checkDefault(@RequestParam("uid")String uid , @RequestParam("addressId")Long addressId){ //세션으로변경 예정

        addressService.checkDefault(addressId, uid);
        return "redirect:/address/detail";
    }

    @PostMapping("/delete") //주소지 삭제
    public String addressDelete(@RequestParam("addressId")Long addressId){
        addressService.deleteAddress(addressId);

        return "redirect:/address/detail";
    }
}
