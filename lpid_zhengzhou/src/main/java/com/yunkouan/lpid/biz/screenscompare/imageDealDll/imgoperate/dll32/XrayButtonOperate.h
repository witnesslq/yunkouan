#ifndef _FINETUNE_H
#define _FINETUNE_H

#define PROC_API extern "C" _declspec(dllexport)

//imageIn: input image.
//method:1,gray;2,color;13,gray enhance;23,color enhance;
//14,gray bright;24,color bright;15,gray dull;25,color dull;16,gray super enhance;26,color super enhance;
//27,no organics;28,no inorganic;19,gray reverse;29,color reverse
PROC_API void  ColorFineTune(char* imageIn,int method,char* imageOut);

#endif