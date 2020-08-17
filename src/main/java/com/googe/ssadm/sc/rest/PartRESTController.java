package com.googe.ssadm.sc.rest;

import com.googe.ssadm.sc.entity.Part;
import com.googe.ssadm.sc.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PartRESTController {

    private final PartService partService;

    @Autowired
    public PartRESTController(PartService partService) {
        this.partService = partService;
    }


    @GetMapping("/getparts")
    public List<SelectItem> partItems(@RequestParam(value = "q", required = false) String query){
        if(StringUtils.isEmpty(query)){
            return partService.findAll()
                    .stream()
                    .filter(Part::isPartOrWork)
                    .map(this :: mapToSelectItem)
                    .distinct()
                    .limit(15)
                    .collect(Collectors.toList());
        }

        return partService.findAll()
            .stream()
                .filter(Part::isPartOrWork)
                .filter(part -> part.getPartNo()
                    .toLowerCase()
                    .contains(query))
                .map(this :: mapToSelectItem)
                .distinct()
                .limit(15)
                .collect(Collectors.toList());
    }

    @GetMapping("/getserial")
    public List<SelectItem> getSerials(@RequestParam(value = "q", required = false) String query){
        if(StringUtils.isEmpty(query)){
            return partService.findAll()
                    .stream()
                    .filter(part -> part.isOnStock())
                    .filter(part -> !part.isPartOrWork())
                    .map(this :: mapSerialNoToSelectItem)
                    .distinct()
                    .limit(15)
                    .collect(Collectors.toList());
        }

        return partService.findAll()
                .stream()
                .filter(part -> part.isOnStock())
                .filter(part -> !part.isPartOrWork())
                .filter(part -> part.getSerialNo()
                        .toLowerCase()
                        .contains(query))
                .map(this :: mapSerialNoToSelectItem)
                .distinct()
                .limit(15)
                .collect(Collectors.toList());
    }

    private SelectItem mapToSelectItem(Part part){
        return SelectItem.builder()
                .id(Long.toString(part.getId()))
                .text(part.getPartNo())
                .build();

    }

    private SelectItem mapSerialNoToSelectItem(Part part){
        return SelectItem.builder()
                .id(Long.toString(part.getId()))
                .text(part.getSerialNo())
                .build();

    }

}
