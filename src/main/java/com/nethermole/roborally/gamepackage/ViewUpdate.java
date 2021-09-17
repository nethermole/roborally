package com.nethermole.roborally.gamepackage;

import com.nethermole.roborally.StartInfo;
import lombok.Data;

import java.util.List;

@Data
public class ViewUpdate {

    private List<ViewStep> viewSteps;

    private StartInfo startInfo;

}
