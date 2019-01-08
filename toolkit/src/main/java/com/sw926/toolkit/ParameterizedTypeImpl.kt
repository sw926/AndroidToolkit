package com.sw926.toolkit

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Suppress("unused")
class ParameterizedTypeImpl private constructor(private val rawType: Type, typeArguments: Array<Type>?) : ParameterizedType {

    companion object {

        fun get(rawType: Type, vararg typeArguments: Type): ParameterizedTypeImpl {
            val list = mutableListOf<Type>()
            typeArguments.forEach {
                list.add(it)
            }
            return ParameterizedTypeImpl(rawType, list.toTypedArray())
        }
    }

    private val typeArguments: Array<Type>?

    init {
        this.typeArguments = canonicalize(typeArguments)
    }

    private fun canonicalize(typeArguments: Array<Type>?): Array<Type>? {
        if (typeArguments != null && typeArguments.size > 1) {
            return arrayOf(ParameterizedTypeImpl(typeArguments[0], typeArguments))
        }
        return typeArguments
    }

    override fun getActualTypeArguments(): Array<Type> {
        return typeArguments!!.clone()
    }

    override fun getOwnerType(): Type? {
        return null
    }

    override fun getRawType(): Type {
        return rawType
    }

    fun typeToString(type: Type): String {
        return if (type is Class<*>) type.name else type.toString()
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder(30 * (typeArguments!!.size + 1))
        stringBuilder.append(typeToString(rawType))

        if (typeArguments.isEmpty()) {
            return stringBuilder.toString()
        }

        stringBuilder.append("<").append(typeToString(typeArguments[0]))
        for (i in 1 until typeArguments.size) {
            stringBuilder.append(", ").append(typeToString(typeArguments[i]))
        }
        return stringBuilder.append(">").toString()
    }


}